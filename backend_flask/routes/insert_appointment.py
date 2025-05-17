from flask import Blueprint, request
from db_config import get_db_connection

insert_appointment = Blueprint('insert_appointment', __name__)

@insert_appointment.route('/insert_appointment', methods=['POST'])
def insert():
    user_email = request.form.get("user_email")
    date = request.form.get("date")
    time = request.form.get("time")
    appt_type = request.form.get("type")

    if not all([user_email, date, time, appt_type]):
        return "fail"

    db = get_db_connection()
    cursor = db.cursor()

    try:
        cursor.execute(
            "INSERT INTO appointments (user_email, date, time, type) VALUES (%s, %s, %s, %s)",
            (user_email, date, time, appt_type)
        )
        db.commit()
        return "success"
    except:
        return "fail"
