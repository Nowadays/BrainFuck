package com.deodexed.brainfuck;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Deode on 14/09/2016.
 */
    class HttpPostTask extends AsyncTask<String, Integer, String>
    {
        /** écouteur sur la terminaison de la requête HTTP */
        private RemoteDatabaseListener listener;
        /** script PHP à ouvrir */
        private String scriptToExecute;
        /** paramètres à fournir au script PHP */
        private ContentValues params;
        public String BASE_URL = "http://deodexed.fr/BrainFuck/index.php/";

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        public HttpPostTask(RemoteDatabaseListener listener, String scriptToExecute, ContentValues params)
        {
            this.listener = listener;
            this.scriptToExecute = scriptToExecute;
            this.params = params;
        }


        @Override
        protected String doInBackground(String... values)
        {
            // connexion avec le serveur
            HttpURLConnection connexion = null;
            try {
                // ouvrir la connexion HTTP
                URL url = new URL(BASE_URL + scriptToExecute);
                connexion = (HttpURLConnection) url.openConnection();

                // configurer la connexion en mode POST
                connexion.setDoOutput(true);
                connexion.setDoInput(true);
                connexion.setReadTimeout(10000);
                connexion.setConnectTimeout(15000);
                connexion.setRequestMethod("POST");
               // connexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                String sb = encodeParameters(params,true);
                connexion.setFixedLengthStreamingMode(sb.length());
                DataOutputStream contenu = new DataOutputStream(connexion.getOutputStream());
                contenu.writeBytes(sb.toString());
               // contenu.close();

                // récupérer la réponse du serveur
                String serverResponseMessage = connexion.getResponseMessage();
                InputStream reponse = connexion.getInputStream();
                // récupérer les données et en faire une chaîne
               BufferedReader reader = new BufferedReader(new InputStreamReader(reponse, "utf-8"));
                String jsondata = reader.readLine();
                Log.i("TEST", jsondata);
                contenu.close();
                return null;

            } catch (Exception e) {
                Log.e("test", e.toString());
                return null;
            } finally {
                // déconnexion obligatoire pour ne pas bloquer le serveur
                if (connexion != null) connexion.disconnect();
            }
        }

        /**
         * Cette méthode est appelée quand doInBackground est terminée. Son paramètre est la réponse du serveur.
         //* @param données à transmettre à l'écouteur, ce sont des données JSON, éventuellement null
         */
        @Override
        protected void onPostExecute(String jsondata)
        {

        }

        private static String encodeParameters(ContentValues params, boolean separator) throws UnsupportedEncodingException {
            if (params == null) return "";
            String separ = "";
            StringBuilder result = new StringBuilder();

            for (String nom: params.keySet()){
                result.append(separ);
                separ = "&";

                result.append(URLEncoder.encode(nom,"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.getAsString(nom),"UTF-8"));
            }

            return result.toString();
        }
    }

