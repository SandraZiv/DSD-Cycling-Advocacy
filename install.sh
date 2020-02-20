

sudo -u bumpy virtualenv backend/venv -q -p python3
backend/venv/bin/pip install -r backend/requirements.txt

cat << EOF > /etc/systemd/system/bumpy-backend.service
[Unit]
Description=Gunicorn instance to serve bumpy-backend
After=network.target

[Service]
User=bumpy
Group=www-data
WorkingDirectory=/home/bumpy/bumpy/DSD-Cycling-Advocacy/backend
Environment="PATH=/home/bumpy/bumpy/DSD-Cycling-Advocacy/backend/venv/bin"
ExecStart=/home/youruser/bumpy/DSD-Cycling-Advocacy/backend/venv/bin/gunicorn --workers 3 --bind unix:bumpy-backend.sock -m 007 wsgi:app

[Install]
WantedBy=multi-user.target
EOF