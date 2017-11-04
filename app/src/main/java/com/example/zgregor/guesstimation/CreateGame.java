package com.example.zgregor.guesstimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.support.v7.app.AppCompatActivity;
import java.lang.String;

public class CreateGame extends AppCompatActivity {

    public Connection con;
    public String newGameName = "";
    public String newGamePassword = "";
    public Button createGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        createGame = (Button) findViewById(R.id.createGame);
        createGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin cl = new CheckLogin();
                EditText gameTextView = (EditText) findViewById(R.id.gameText);
                newGameName = gameTextView.getText().toString();
                EditText passwordTextView = (EditText) findViewById(R.id.passwordText);
                newGamePassword = passwordTextView.getText().toString();
                cl.gameName = newGameName;
                cl.gamePassword = newGamePassword;
                cl.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String status = "";
        String gameName = "";
        String gamePassword = "";
        Boolean isSuccess = false;

        //public CheckLogin(String a, String b)
        @Override
        protected void onPostExecute(String r)
        {
            if(isSuccess)
            {
                Intent intent = new Intent (getApplicationContext(), HostedGames.class);
                startActivity(intent);
            }
            else {
                TextView errorUpdate = (TextView) findViewById(R.id.errorView);
                errorUpdate.setText("You need a name and password to create a new game.");
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                con = connectionclass();        // Connect to database
                if (con == null)
                {
                    status = "Check Your Internet Access!";
                }
                else
                {
                    // Change below query according to your own database.
                    if(!gameName.equals("") && !gamePassword.equals("")) {
                        //use parameterized query
                        String query = "insert into game (GameName, GamePass) values ('" + gameName + "', '" + gamePassword + "')";
                        Statement stmt = con.createStatement();
                        stmt.execute(query);
                        status = ("Game created successfully");
                        isSuccess = true;
                    }
                    else{
                        isSuccess = false;
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                status = ex.getMessage();

                Log.d ("sql error", status);
            }

            return status;
        }
    }


    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            ConnectionURL = "jdbc:jtds:sqlserver://petesprogrammers.database.windows.net;DatabaseName=petesprogrammers;user=admincowboy;password=Notverysecure1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

}
