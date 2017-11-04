package com.example.zgregor.guesstimation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class HostedGames extends AppCompatActivity {

    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;
    public final static String Extra_String = "com.example.zgregor.guesstimation.Extra";
    public Connection con;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosted_games);
        //get the userID and username passed from the login page intent
        userID = getIntent().getStringExtra(Login.Extra_String);
        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, R.layout.row, R.id.row, arrayListToDo);
        ListView listView = (ListView) findViewById(R.id.currentGamesLv);
        listView.setAdapter(arrayAdapterToDo);
        ShowGames showGames = new ShowGames();
        showGames.execute("");

        //TextView userText = (TextView) findViewById(R.id.nameText);
        //userText.setText(userID);
    }

    public void editButtonClick(View v)
    {
        Intent intent = new Intent (getApplicationContext(), EditGame.class);
        startActivity(intent);
        String a = userID;
        intent.putExtra(Extra_String, a);
    }

    public void CreateGameButtonClick(View v) {
        Intent intent = new Intent (getApplicationContext(), CreateGame.class);
        startActivity(intent);
    }

    public void HomeButtonClick(View v) {
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public class ShowGames extends AsyncTask<String,String,ArrayList<String>> {
        String status = "";


        //public CheckLogin(String a, String b)
        @Override
        protected void onPostExecute(ArrayList<String> r) {
            Iterator<String> iterator = r.iterator();
            while (iterator.hasNext()) {
                arrayAdapterToDo.add(iterator.next().toString());
            }


        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> games = null;
            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    status = "Check Your Internet Access!";
                } else {
                    // Change below query according to your own database.
                    //use parameterized query
                    //check if password matches the username given if so pass the username on
                    String query = "SELECT Game.GameID, Game.GameName, GameMaker.GameMakerID FROM Game INNER JOIN GameMaker ON Game.GameMakerID = GameMaker.GameMakerID";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    games = new ArrayList<String>();
                    while (rs.next()) {
                        String a = String.valueOf(rs.getInt(1));
                        String b = rs.getString(2);

                        games.add(a + " |   " + b);
                    }

                }

            } catch (SQLException e) {
                Log.d("sql error", e.getMessage());
            }
            return games;
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
