
#get the root directory path of the project files 
#here we get it by getting the location of this script in the runtime environment
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cat << EOF > /etc/systemd/system/bumpy-backend.service
[Unit]
Description=Gunicorn instance to serve bumpy-backend
After=network.target

[Service]
User=bumpy
Group=www-data
WorkingDirectory=$DIR/backend
Environment="PATH=$DIR/backend/venv/bin"
ExecStart=PATH=$DIR/backend/venv/bin/gunicorn --workers 3 --bind unix:bumpy-backend.sock -m 007 wsgi:app

[Install]
WantedBy=multi-user.target
EOF