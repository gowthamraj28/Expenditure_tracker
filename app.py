from flask import Flask, render_template, request,redirect,url_for,flash,session
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime
from werkzeug.security import generate_password_hash,check_password_hash
from io import BytesIO
from reportlab.lib.pagesizes import A4,letter
from reportlab.pdfgen import canvas
from reportlab.platypus import SimpleDocTemplate,Table,TableStyle,Paragraph
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet
from flask import send_file
import os


app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db' 
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = os.environ.get('SECRET_KEY','dev_key_for_local')
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(30), nullable=False)
    email = db.Column(db.String(20), nullable=False, unique=True)
    password = db.Column(db.String(200), nullable=False)
    items = db.relationship('Item',backref='owned_user',lazy=True)

    def __init__(self,name,email,password):
        self.name = name
        self.email = email
        self.password = password



class Item(db.Model):
    id = db.Column(db.Integer(),primary_key=True)
    itemname = db.Column(db.String(50),nullable=False)
    amount = db.Column(db.Float(),nullable=False)
    date = db.Column(db.Date,nullable = False)
    user_id = db.Column(db.Integer(), db.ForeignKey('user.id'),nullable = False)

    def __init__(self,itemname,amount,date,user_id):
        self.itemname = itemname
        self.amount = amount
        self.date = date
        self.user_id = user_id


@app.route('/')
def home():
    return render_template("home.html")


@app.route('/register',methods=['GET','POST'])
def register():
    if 'user_id' in session:
        return redirect(url_for('tracker'))
    

    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        confirm_password = request.form['password1']


        if password != confirm_password:
            flash("Password doesn't match!!")
            return redirect(url_for('register'))
        
        existing_user = User.query.filter_by(email = email).first()
        if existing_user:
            flash("Account already registered!")
            return redirect(url_for('login'))
        
        hashed_pw = generate_password_hash(password)        
        new_user = User(name = name, email = email, password = hashed_pw)
        db.session.add(new_user)
        db.session.commit()

        flash("Account created successfully!")
        return redirect(url_for('login'))
        
    return render_template("register.html")



@app.route('/login', methods=['GET', 'POST'])
def login():
    if 'user_id' in session:
        return redirect(url_for('tracker'))
    
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        user = User.query.filter_by(email = email).first()

        if user and check_password_hash(user.password, request.form['password']):
            session['user_id'] = user.id
            flash('Logged in successfully')
            return redirect(url_for('tracker'))
        else:
            flash("Invalid email or password!")
            return redirect(url_for('login'))
        
    return render_template("login.html")


@app.route('/tracker',methods=['GET','POST'])
def tracker():
    if 'user_id' not in session:
        flash("You need to login first.")
        return redirect(url_for('login'))
    
    user_id = session['user_id']
    user = User.query.get(user_id)

    if request.method == 'POST':
        itemname = request.form['itemname']
        amount = float(request.form['amount'])
        date = datetime.strptime(request.form['date'],'%Y-%m-%d')

        new_item = Item(itemname=itemname,amount=amount,date=date,user_id=user_id)
        db.session.add(new_item)
        db.session.commit()
        flash("Expense added!")

    items = Item.query.filter_by(user_id = user_id).order_by(Item.date.desc()).all()
    return render_template("tracker.html",user = user,items = items,editing_item = None)



@app.route('/generate_report',methods=['POST'])
def generate_report():

    start_date_str = request.form.get('start_date')
    end_date_str = request.form.get('end_date')

    if not start_date_str or not end_date_str:
        flash("Please Provide both start and end dates.","warning")
        return redirect(url_for('tracker'))
    
    try:
        start_date = datetime.strptime(start_date_str,"%Y-%m-%d").date()
        end_date = datetime.strptime(end_date_str,"%Y-%m-%d").date()

        if start_date > end_date:
            flash("Start Date Cannot be after End date!","danger")
            return redirect(url_for('tracker'))

        items = Item.query.filter(
                Item.user_id == session.get('user_id'),
                Item.date>=start_date,
                Item.date<=end_date
        ).all()

        if not items:
            flash("No recored found for the selected range","info")
            return redirect(url_for('tracker'))

        return render_template("report.html",items=items,start_date = start_date,end_date = end_date)
    
    except ValueError:
        flash("Invalid date format!","danger")
        return redirect(url_for('tracker'))
    

@app.route('/download_report')
def download_report():
    amount = 0
    start_date_str = request.args.get('start_date')
    end_date_str = request.args.get('end_date')

    start_date = datetime.strptime(start_date_str,"%Y-%m-%d").date()
    end_date = datetime.strptime(end_date_str,"%Y-%m-%d").date()

    if start_date > end_date:
        flash("Start Date Cannot be after End date!","danger")
        return redirect(url_for('tracker'))

    items = Item.query.filter(
            Item.user_id == session.get('user_id'),
            Item.date>=start_date,
            Item.date<=end_date
    ).all()

    buffer = BytesIO()
    doc = SimpleDocTemplate(buffer,pagesize = letter)
    styles = getSampleStyleSheet()
    elements = []

    elements.append(Paragraph(f"Expenditure Report ({start_date} to {end_date})",styles['Heading1']))
    table_data = [["Date","ItemName","Amount"]]
    for item in items:
        table_data.append([item.date.strftime("%Y-%m-%d"),item.itemname,f"{item.amount:.2f}"])
        amount += item.amount
    
    table_data.append(["","Total",f"{amount:.2f}"])

    page_width = letter[0]
    table_width = page_width*0.65
    col_width = [table_width/3]*3

    table = Table(table_data,repeatRows=1,colWidths=col_width)
    table.setStyle(TableStyle([
        ('BACKGROUND',(0,0),(-1,0),colors.grey),
        ('TEXTCOLOR',(0,0),(-1,0), colors.whitesmoke),
        ('BACKGROUND',(0,1),(-1,-1),colors.white),
        ('TEXTCOLOR',(0,1),(-1,-1),colors.black),
        ('ALIGN',(0,0),(-1,-1),'CENTER'),
        ('BACKGROUND',(0,-1),(-1,-1),colors.white),
        ('GRID',(0,0),(-1,-1),1,colors.black)
    ]))
    elements.append(table)
    doc.build(elements)
    buffer.seek(0)
    return send_file(buffer, as_attachment=True,download_name='expenditure_report.pdf',mimetype = 'application/pdf')
    

@app.route('/edit/<int:item_id>',methods = ['GET','POST'])
def edit(item_id):
    item = Item.query.get_or_404(item_id)

    if item.user_id != session.get('user_id'):
        flash("Unauthorized access!","danger")
        return redirect(url_for('tracker'))
    
    if request.method == 'POST':
        item.itemname = request.form['itemname']
        item.amount = float(request.form['amount'])
        item.date = datetime.strptime(request.form['date'],"%Y-%m-%d")

        db.session.commit()
        flash("Item updated successfully!","success")
        return redirect(url_for('tracker'))
    
    user_id = session.get('user_id')
    user = User.query.get(user_id)
    items = Item.query.filter_by(user_id=user_id).order_by(Item.date.desc()).all()

    return render_template('tracker.html',editing_item=item,items = items,user = user)


@app.route('/delete/<int:item_id>')
def delete(item_id):
    item = Item.query.get_or_404(item_id)

    if item.user_id != session.get('user_id'):
        flash("Unauthorized access!","danger")
        return redirect(url_for('tracker'))
    
    db.session.delete(item)
    db.session.commit()
    flash("Item deleted succesfully!","success")
    return redirect(url_for('tracker'))


@app.route('/logout')
def logout():
    session.clear()
    flash("Logged out!")
    return redirect(url_for('home'))


@app.route('/database')
def database():
    users = User.query.all()
    return render_template("database.html", users = users)


if __name__ == '__main__':
    with app.app_context():
        db.create_all()  
    app.run(debug=True)
