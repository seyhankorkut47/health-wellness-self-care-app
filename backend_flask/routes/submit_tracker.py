from flask import Blueprint, request
from db_config import get_db_connection

submit_tracker = Blueprint('submit_tracker', __name__)

@submit_tracker.route('/submit_tracker', methods=['POST'])
def submit():
    user_email = request.form.get("user_email")
    sleep_hours = request.form.get("sleep_hours")
    water_intake = request.form.get("water_intake")
    exercised = request.form.get("exercised")

    if not all([user_email, sleep_hours, water_intake, exercised]):
        return "fail"

    db = get_db_connection()
    cursor = db.cursor()

    try:
        cursor.execute(
            "INSERT INTO tracker_data (user_email, sleep_hours, water_intake, exercised) VALUES (%s, %s, %s, %s)",
            (user_email, sleep_hours, water_intake, exercised)
        )
        db.commit()
        return "success"
    except Exception as e:
        print("Tracker insert error:", e)  # Hata loglama
        return "fail"
    finally:
        cursor.close()
        db.close()

