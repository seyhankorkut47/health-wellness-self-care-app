from flask import Blueprint, request, jsonify
from db_config import get_db_connection

get_appointments = Blueprint('get_appointments', __name__)

@get_appointments.route('/get_appointments', methods=['GET'])
def get_appointments_func():
    email = request.args.get("email")

    if not email:
        return jsonify([])

    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT date, time, type FROM appointments WHERE user_email = %s", (email,))
    rows = cursor.fetchall()

    result = []
    for row in rows:
        date = str(row[0])
        time = str(row[1])  # 👈 timedelta yerine string'e çevir
        appt_type = row[2]
        result.append({"date": date, "time": time, "type": appt_type})

    return jsonify(result)
