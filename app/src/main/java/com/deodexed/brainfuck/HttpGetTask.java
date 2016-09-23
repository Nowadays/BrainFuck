package com.deodexed.brainfuck;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Deode on 11/09/2016.
 */

    class HttpGetTask extends AsyncTask<String, Integer, String>
    {
        /** écouteur sur la terminaison de la requête HTTP */


        private RemoteDatabaseListener listener;
        /** script PHP à ouvrir */
        private String scriptPath;
        private String scriptConstant;
        private ProgressDialog pd;
        /** paramètres à fournir au script PHP */

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        private String BASE_URL = "http://deodexed.fr/BrainFuck/index.php/";

        /**
         * initialise un gestionaire de HttpURLConnection asynchrone et définit l'écouteur à prévenir
       //  * @param listener : écouteur à prévenir quand la requête est terminée
         * @param script : nom du script PHP à exécuter sur le serveur
         * @param params : paramètres de la requête
         */
        public HttpGetTask(RemoteDatabaseListener remoteDatabaseListener, String scriptPath, String scriptConstant, ProgressDialog pd)
        {
            //this.listener = listener;
            this.listener = remoteDatabaseListener;
            this.scriptPath = scriptPath;
            this.scriptConstant = scriptConstant;
            this.pd = pd;
        }

        /**
         * Cette méthode contient la tâche qu'il faut effectuer en arrière-plan. Ici, c'est une connexion
         * de type HttpURLConnection à gérer : ouvrir, configurer, attendre puis extraire la réponse.
         * @return les données JSON retournées par le serveur
         */
        @Override
        protected String doInBackground(String... values)
        {
            // connexion avec le serveur
            HttpURLConnection connexion = null;
            URL url = null;
            try {
                // ouvrir la connexion HTTP
                url = new URL(BASE_URL + scriptPath);
                connexion = (HttpURLConnection) url.openConnection();

                // configurer la connexion en mode GET
                connexion.setReadTimeout(10000);
                connexion.setConnectTimeout(15000);
                connexion.setRequestMethod("GET");
                connexion.setDoInput(true);

                // récupérer la réponse du serveur
                InputStream reponse = connexion.getInputStream();

                // récupérer les données et en faire une chaîne
                BufferedReader reader = new BufferedReader(new InputStreamReader(reponse, "utf-8"));
                String jsondata = "";
                String fromReader = "";


                while ((fromReader = reader.readLine()) != null) {
                    // Add line to jsonString
                    jsondata += (fromReader + "\n");
                }

                pd.dismiss(); //ProgressDialog started from the main thread

                return jsondata;

            } catch (Exception e) {

                    Log.e("ERROR", e.toString());

                return null;

            } finally {
                // déconnexion obligatoire pour ne pas bloquer le serveur
                if (connexion != null) connexion.disconnect();
            }
        }

        /**
         * Cette méthode est appelée quand doInBackground est terminée. Son paramètre est la réponse du serveur.
        // * @param données à transmettre à l'écouteur, ce sont des données JSON, éventuellement null
         */
        @Override
        protected void onPostExecute(String jsondata)
        {
            // s'il y a un écouteur, lui signaler que le travail est fini
            if(jsondata != null) {
              listener.onExecSQLfinished(jsondata, scriptConstant);
            }
        }

        private static String encodeParameters(ContentValues params) throws UnsupportedEncodingException {
            if (params == null) return "";
            StringBuilder result = new StringBuilder();

            for (String nom: params.keySet()){
                result.append(URLEncoder.encode(nom,"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.getAsString(nom), "UTF-8"));
            }

            return result.toString();
        }

    }

