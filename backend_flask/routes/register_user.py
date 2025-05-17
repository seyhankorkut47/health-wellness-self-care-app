from flask import Blueprint, request, jsonify
from db_config import get_db_connection
import hashlib

register_user = Blueprint('register_user', __name__)

@register_user.route('/register_user', methods=['POST'])
def register():
    email = request.form.get('email')
    password = request.form.get('password')

    if not email or not password:
        return "fail", 400

    conn = get_db_connection()
    cursor = conn.cursor()

    # Kullanıcı zaten var mı kontrol et
    cursor.execute("SELECT * FROM users WHERE email = %s", (email,))
    if cursor.fetchone():
        return "exists", 200

    # Şifreyi hashleyip veritabanına kaydet
    hashed_password = hashlib.sha256(password.encode()).hexdigest()
    cursor.execute("INSERT INTO users (email, password) VALUES (%s, %s)", (email, hashed_password))
    conn.commit()
    cursor.close()
    conn.close()

    return "success", 200
