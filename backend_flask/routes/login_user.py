from flask import Blueprint, request, jsonify
from db_config import get_db_connection
import hashlib

login_user = Blueprint('login_user', __name__)

@login_user.route('/login_user', methods=['POST'])
def login():
    email = request.form.get("email")
    password = request.form.get("password")

    if not email or not password:
        return "fail"

    db = get_db_connection()
    cursor = db.cursor()

    cursor.execute("SELECT password FROM users WHERE email = %s", (email,))
    result = cursor.fetchone()

    if result:
        stored_password = result[0]
        if stored_password == hashlib.sha256(password.encode()).hexdigest():
            return "success"
        else:
            return "fail"
    else:
        return "fail"
