#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -p port -i ip -s suffix "
   echo -e "\t-p Port on which to serve bumpy"
   echo -e "\t-i public ip/domain which will be exposed to the internet"
   echo -e "\t-s suffix to add to the root domain when serving eg:root/suffix"
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
cat << EOF > $DIR/frontend/bumpy-web/build/runtime-config.js
window['runConfig'] = {
    apiRoot: '/$suffix/api/v1'
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
echo "Android app has been configured as well. You may build and publish it"