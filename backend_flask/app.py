from flask import Flask
from flask_cors import CORS

from routes.register_user import register_user
from routes.login_user import login_user
from routes.insert_appointment import insert_appointment
from routes.get_appointments import get_appointments
from routes.submit_tracker import submit_tracker
from routes.insert_reminders import insert_reminders
from routes.get_tracker_data import get_tracker_data
from routes.get_reminders import get_reminders




app = Flask(__name__)
CORS(app)

# URL'leri bağla
app.register_blueprint(get_reminders)
app.register_blueprint(get_tracker_data)
app.register_blueprint(register_user)
app.register_blueprint(login_user)
app.register_blueprint(insert_appointment)
app.register_blueprint(get_appointments)
app.register_blueprint(submit_tracker)
app.register_blueprint(insert_reminders)

if __name__ == "__main__":
    app.run(debug=True)
