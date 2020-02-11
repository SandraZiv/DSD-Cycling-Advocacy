#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -p port -i ip "
   echo -e "\t-p Port on which to serve bumpy"
   echo -e "\t-i public ip/domain which will be exposed to the internet"
   echo -e "\t-r suffix to add to the root domain when serving eg:root/suffix"
   exit 1 # Exit script after printing help
}

while getopts "p:i:r" opt
do
   case "$opt" in
      p ) port="$OPTARG" ;;
      i ) ipname="$OPTARG" ;;
      r ) suffix="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$port" ] || [ -z "$ipname" ] || [ -z "$suffix" ]
then
   echo "Some or all of the parameters are empty";
   helpFunction
fi

# Begin script in case all parameters are correct

echo "Configuring bumpy on $ipname:$port/$suffix "

#get the root directory path of the project files 
#here we get it by getting the location of this script in the runtime environment
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

#configure nginx
cat << EOF > /etc/nginx/sites-available/bumpy
server {
    listen $port;
    server_name $ipname;
    
    location = /$suffix/api {
        return 302 /api/;
    }

    location /$suffix/api/ {
        include proxy_params;
        proxy_pass http://unix:/$DIR/backend/bumpy-backend.sock:/;
    }

    location = /$suffix {
        alias $DIR/frontend/bumpy-web/build;
        index index.html;
    }

}
EOF

#configure frontend

#configure android 