package com.example.mobileapp.ui.setting;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobileapp.R;


public class SettingFragment extends Fragment implements  SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private float maxValue;
    Uri newUri;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        MediaPlayer mp= MediaPlayer.create(getActivity(), R.raw.sound1);

        TextView r=view.findViewById(R.id.text_setting);
        Switch Volume,Notifica,Runstatic;
        CheckBox checkBox;
        SeekBar seekbar;

        r.setText("Setting Fragment");
        try {
            if (checkSystemWritePermission()) {
                RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, newUri);
                Toast.makeText(getContext(), "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i("ringtoon",e.toString());
            Toast.makeText(getContext(), "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
        }
        checkBox = (CheckBox) view.findViewById(R.id.sensor);

        Runstatic = (Switch) view.findViewById(R.id.Runtastic);
        Volume = (Switch) view.findViewById(R.id.audio);
        Notifica = (Switch) view.findViewById(R.id.notifica);
        seekbar = (SeekBar) view.findViewById(R.id.volume);


        //development

      //  sensorManager = (SensorManager) getActivity().getSystemService(Service.SENSOR_SERVICE);


        sensorManager=(SensorManager) view.getContext().getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(getActivity(), "Device not light sensor", Toast.LENGTH_SHORT).show();

        }

        new Thread()
        {
            @Override
            public void run()
            {


        checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    Toast.makeText(getActivity(), "Sensor active" , Toast.LENGTH_SHORT).show();
                    maxValue = lightSensor.getMaximumRange();

                    lightEventListener = new SensorEventListener() {


                        @Override
                        public void onSensorChanged(SensorEvent sensorEvent) {
                            float value = sensorEvent.values[0];

                            // between 0 and 255
                            int newValue = (int) (255f * value / maxValue);
                           // view.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
                            int currBrightness = Settings.System.getInt(getActivity().getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,newValue);
//                            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                            // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
                            // It will also change the screen brightness for the device.
  //                          Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, currBrightness);

                            // Set current screen brightness value to seekbar progress.
                            seekbar.setProgress(currBrightness);


                        }


                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }

                    };                // perform logic
                }else{

                    Toast.makeText(getActivity(), "False ", Toast.LENGTH_SHORT).show();
                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                            Context context = getActivity().getApplicationContext();

                            boolean canWriteSettings = Settings.System.canWrite(context);

                            if(canWriteSettings) {

                                int screenBrightnessValue = i*255/100;

                                // Set seekbar adjust screen brightness value in the text view.

                                // Change the screen brightness change mode to manual.
                                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
                                // It will also change the screen brightness for the device.
                                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);
                            }else
                            {
                                // Show Can modify system settings panel to let user add WRITE_SETTINGS permission for this app.
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });


      //              seekbar.setProgress(cBrightness);

                }


            }

        });
            }
        }.start();
        //Getting Current screen brightness.

        Notifica.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked)
                    Toast.makeText(getActivity(), "Notifica on", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Notifica off", Toast.LENGTH_SHORT).show();

            }
        });


//volume


        Volume.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked)
                {
                    Toast.makeText(getActivity(), "Volume on", Toast.LENGTH_SHORT).show();
                    unmute();


                     mp.start();



                }
                else {

                        mute();
                    Toast.makeText(getActivity(), "Volume off", Toast.LENGTH_SHORT).show();
                    mp.pause();
                }
            }
        });
//runstatic
        Runstatic.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked)
                    Toast.makeText(getActivity(), "Volume on", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Volume off", Toast.LENGTH_SHORT).show();

            }
        });
       //            checkBox.setChecked(false); settato a falso





            return view;
    }
    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(getContext()))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            getContext().startActivity(intent);
        }
    }
    private void mute(){
        //mute audio
        AudioManager amanager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    }

    public void unmute() {
        //unmute audio
        AudioManager amanager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);


    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
