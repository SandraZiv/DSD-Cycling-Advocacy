#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -p port -i ip -s suffix "
   echo -e "\t-p Port on which to serve bumpy"
   echo -e "\t-i public ip/domain which will be exposed to the internet"
   echo -e "\t-s suffix to add to the root domain when serving eg: bumpy"
   exit 1 # Exit script after printing help
}

while getopts "p:i:s:" opt
do
   case "$opt" in
      p ) port="$OPTARG" ;;
      i ) ipname="$OPTARG" ;;
      s ) suffix="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Notify in case parameters are empty
if [ -z "$port" ] || [ -z "$ipname" ] || [ -z "$suffix" ]
then
   echo "Some or all of the parameters are empty";
fi

# Begin script in case all parameters are correct
echo ""
echo "Configuring bumpy on $ipname:$port/$suffix "
echo ""
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

    location /$suffix {
        alias $DIR/frontend/bumpy-web/build;
        index index.html;
    }
}
EOF

#configure frontend
cat << EOF > $DIR/frontend/bumpy-web/build/runtime-config.js
window['runConfig'] = {
    apiRoot: '/$suffix/api/v1'
}
EOF

#in case of a different suffix and rebuilding
cat << EOF > $DIR/frontend/bumpy-web/public/runtime-config.js
window['runConfig'] = {
    apiRoot: '/$suffix/api/v1'
}
EOF

cat << EOF > $DIR/frontend/bumpy-web/package.json
{
  "name": "bumpy-web",
  "version": "0.1.0",
  "proxy": "http://161.53.67.132:5000",
  "homepage": "/bumpy",
  "private": true,
  "dependencies": {
    "bootstrap": "^4.4.1",
    "fetch-absolute": "^1.0.0",
    "history": "latest",
    "http-proxy-middleware": "^0.20.0",
    "jquery": "^3.4.1",
    "leaflet": "^1.6.0",
    "leaflet-routing-machine": "^3.2.12",
    "react": "^16.12.0",
    "react-bootstrap": "^1.0.0-beta.16",
    "react-bootstrap-table-next": "^3.3.1",
    "react-bootstrap-table2-editor": "^1.4.0",
    "react-bootstrap-table2-paginator": "^2.1.0",
    "react-dom": "^16.12.0",
    "react-leaflet": "^2.6.0",
    "react-leaflet-control": "^2.1.1",
    "react-router-dom": "^5.1.2",
    "react-scripts": "^3.3.0"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  },
  "eslintConfig": {
    "extends": "react-app"
  },
  "browserslist": {
    "production": [
      ">0.2%",
      "not dead",
      "not op_mini all"
    ],
    "development": [
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ]
  }
}
EOF

#configure android
cat << EOF > $DIR/android/Bumpy/app/src/main/java/com/cycling_advocacy/bumpy/net/service/BumpyServiceBuilder.java

package com.cycling_advocacy.bumpy.net.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BumpyServiceBuilder {

    private static final String BASE_URL = "http://$ipname:$port/$prefix/api/v1/";

    private static Retrofit retrofit = createRetrofit();

    private static Retrofit createRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTC())
                .create();

        Retrofit.Builder retrofitBuiler = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build());
        return retrofitBuiler.build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
EOF

echo "Configuration complete. Your app has been configured to be served from $ipname:$port/$prefix"
echo "You will need to restart nginx: run 'sudo systemctl restart nginx'"
echo "NOTE: If you chose a suffix that is not 'bumpy' you will need to re-run node build. Position yourself into $DIR/frontend/bumpy-web and run 'node run build' "
echo "Android app has been configured as well. You may build and publish it"
