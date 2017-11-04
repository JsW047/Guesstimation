package com.example.zgregor.guesstimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateAccount extends AppCompatActivity {
    public Connection con;
    public String newUsername;
    public String newPassword;
    public Button createUser;


    public void LoginButtonClick(View v) {
        Intent intent = new Intent (getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createUser = (Button) findViewById(R.id.createAccount);
        createUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CheckLogin cl = new CheckLogin();
                EditText unameText = (EditText) findViewById(R.id.usernameText);
                newUsername = unameText.getText().toString();
                EditText pwordText = (EditText) findViewById(R.id.passwordText);
                newPassword = pwordText.getText().toString();
                cl.username = newUsername;
                cl.password = newPassword;
                cl.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String status = "";
        String username = "";
        String password = "";
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
                errorUpdate.setText("You need a name and password to create an account.");
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
                    if(!username.equals("") && !password.equals("")) {
                        //use parameterized query
                        String query = "insert into GameMaker (UserName, Pass) values ('" + username + "', '" + password + "')";
                        Statement stmt = con.createStatement();
                        stmt.execute(query);
                        status = ("Account created successfully");
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
