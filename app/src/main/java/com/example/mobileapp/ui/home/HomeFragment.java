package com.example.mobileapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.app.ListFragment;


import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import static java.lang.Thread.sleep;

public class HomeFragment extends Fragment {
    public void searchBooks(View view) {
        // Get the search string from the input field.

    }

        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            TextView r=view.findViewById(R.id.text_home);
            String book = null;
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Add a book", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Add a new book ");
                    builder.setMessage("Enter a book name or a who wrote it");
                    final EditText input = new EditText(getContext());
                    final TextView output = new TextView(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    input.setHint("Enter a book title");
                    builder.setView(input);
                    builder.setIcon(R.drawable.search_foreground);
                    builder.setPositiveButton("Search",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    // Write your code here to execute after dialog
                                    new Thread()
                                    {
                                        @Override
                                        public void run()
                                        {




                                            String queryString = input.getText().toString();

                                            // Hide the keyboard when the button is pushed.
                                       /*     InputMethodManager inputManager = (InputMethodManager)
                                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputManager.hideSoftInputFromWindow(,
                                                    InputMethodManager.HIDE_NOT_ALWAYS);
*/
                                            // Check the status of the network connection.
                                            ConnectivityManager connMgr = (ConnectivityManager)
                                                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                                            // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
                                            if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
                                                new FetchBook( output,input).execute(queryString);

                                                dialog.cancel();
            }
                                            // Otherwise update the TextView to tell the user there is no connection or no search term.
                                            else {
                                                if (queryString.length() == 0) {
                                                    Snackbar.make(view, "You need to put some more information", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();



                                                }
                                            }
                                        }
                                    }.start();

                                    try {sleep(10);
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                        builder2.setTitle("The query return by get GoogleAPI: ");
                                        builder2.setMessage("If the the response is not ok click 'Return' otherwise click on 'Add' ");

                                        builder2.setView(output);
                                        builder2.setNegativeButton("Return",   new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog
                                                dialog.cancel();
                                            }});
                                        builder2.show();
                                        sleep(1000);
                                        dialog.cancel();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    String trovato=output.getText().toString();
                                    System.out.println(trovato);
                                    dialog.cancel();
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }

            });
           r.setText("Home Fragment");
            return view;
        }


}

