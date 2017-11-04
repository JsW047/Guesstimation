package com.example.zgregor.guesstimation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    public String username;
    public String password;
    public int userID;
    public Connection con;
    public Button loginButton;
    public final static String Extra_String = "com.example.zgregor.guesstimation.Extra";

    public void CreateAccountButtonClick(View v)
    {
        Intent intent = new Intent (getApplicationContext(), CreateAccount.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Login.CheckLogin cl = new Login.CheckLogin();
                EditText usernameTextView = (EditText) findViewById(R.id.usernameText);
                username = usernameTextView.getText().toString();
                EditText passwordTextView = (EditText) findViewById(R.id.passwordText);
                password = passwordTextView.getText().toString();
                cl.checkUsername = username;
                cl.checkPassword = password;
                cl.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String status = "";
        String checkUsername = "";
        String checkPassword = "";
        Boolean isSuccess = false;

        //public CheckLogin(String a, String b)
        @Override
        protected void onPostExecute(String r)
        {
            if(isSuccess)
            {
                Intent intent = new Intent (getApplicationContext(), HostedGames.class);
                String a = Integer.toString(userID);
                intent.putExtra(Extra_String, a);
                //Testing getting the userID and passing it to the HostedGames page with an intent
                startActivity(intent);
            }
            else {
                TextView errorUpdate = (TextView) findViewById(R.id.errorView);
                errorUpdate.setText("Your username or password is incorrect");
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
                        //check if password matches the username given if so pass the username on
                        String query = "Select GameMakerID, Pass from GameMaker where UserName = '" + checkUsername + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        rs.next();
                        String correctPass = rs.getString("Pass");
                        if(correctPass.equals(checkPassword)) {
                            userID = rs.getInt("GameMakerID");
                            status = ("Login successful");
                            rs.close();
                            isSuccess = true;
                        }
                        else {
                            rs.close();
                            isSuccess = false;
                        }
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
