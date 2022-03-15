package com.vkc.loyaltyapp.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vkc.loyaltyapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilityMethods {


    private static GetAccessTokenInterface getTokenIntrface;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

    public static void setErrorForEditTextNull(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                edt.setError(null);

            }

        });
    }

    public static void setErrorForEditText(EditText edt, String msg) {
        edt.setError(msg);
    }


    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static boolean validateLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean isValidPhoneNumber(Context context, CharSequence target) {
        return android.util.Patterns.PHONE.matcher(target).matches();
    }


    public interface GetAccessTokenInterface {
        public void getAccessToken();
    }


    public static String htmlparsing(String text) {
        String encodedString;
        encodedString = text.replaceAll("&lt;", "<");
        encodedString = encodedString.replaceAll("&gt;", ">");
        encodedString = encodedString.replaceAll("&amp;", "");
        encodedString = encodedString.replaceAll("amp;", "");
        return encodedString;
    }


    public static String htmlFile(Context context, String content) {
        String APP_DIR = "." + context.getString(R.string.app_name) + "/";
        String HTMLFILE = "file://APP_DIR/" + content;
        return HTMLFILE;
    }


    public static void hideKeyboardOnTouchOutside(View view,
                                                  final Context context, final EditText edtText) {
        if (!(view instanceof EditText))
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyBoard(context);
                    return false;
                }
            });
    }

    public static int convertDpIntoPixels(Context context, int padding_in_dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        return padding_in_px;
    }

    public static Drawable getButtonDrawableByScreenCathegory(Context con,
                                                              int normalStateResID, int pressedStateResID) {
        Drawable state_normal = con.getResources()
                .getDrawable(normalStateResID).mutate();
        Drawable state_pressed = con.getResources()
                .getDrawable(pressedStateResID).mutate();
        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_pressed},
                state_pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal);
        return drawable;
    }


    public static void editTextValidationAlert(EditText edtText,
                                               String message, Context context) {
        edtText.setError(message);
    }

    public static void setEdtTextTextChangelistener(final EditText edtTxt,
                                                    Context context) {
        edtTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                edtTxt.setError(null);

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtTxt.setError(null);

            }
        });
    }

    public static void txtViewValidationAlert(TextView edtText, String message,
                                              Context context) {
        edtText.setError(message);
    }

    public static void setTxtViewChangelistener(final TextView edtTxt,
                                                Context context) {
        edtTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                edtTxt.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtTxt.setError(null);

            }
        });
    }


    public static String toFile(Bitmap bm, String fileName) {
        String filePath;
        filePath = "/sdcard/" + fileName;
        File f = new File(filePath);
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = null;

            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.close();
            filePath = f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bm = null;
        return filePath;
    }


    public static void setErrorForTextView(TextView edt, String msg) {
        edt.setError(msg);
    }


    public static void textWatcherForEditText(final EditText edt,
                                              final String msg) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0 || s.equals("")) {
                    setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.equals("")) {
                    setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && edt.getError() != null) {
                    edt.setError(null);
                } else if (s.length() == 0 || s.equals("")) {
                    setErrorForEditText(edt, msg);
                }
            }
        });
    }


    public static void textWatcherForTextView(final TextView edt,
                                              final String msg) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && edt.getError() != null) {
                    edt.setError(null);
                } else if (s.length() == 0) {
                    setErrorForTextView(edt, msg);
                }
            }
        });
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((AppCompatActivity) context).getCurrentFocus()
                    .getWindowToken(), 0);

        }
    }

    public static boolean dateComprison(Date inputFirstDate,
                                        Date inputSecondDate) {

        if (inputFirstDate.before(inputSecondDate)) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean dateComprisonToday(Date inputDate) {
        if (inputDate.after(getCurrentDate())) {
            return true;
        } else {
            return false;
        }

    }


    public static Date getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        Date today = new Date(mYear, mMonth, mDay);
        return today;
    }


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int[] getWindowDimens(Context context) {
        int dimen[] = new int[2];
        Point size = new Point();
        WindowManager w = ((AppCompatActivity) context).getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            dimen[0] = size.x;
            dimen[1] = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            dimen[0] = d.getWidth();
            dimen[1] = d.getHeight();
        }
        return dimen;
    }


    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    public static boolean isAppAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static Date getTimeStampMM_DD_YYYY(String inputDate) {
        Date newDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            newDate = formatter.parse(inputDate);
        } catch (ParseException e) {
        }
        return newDate;
    }

    public static String convertMillisecondsToDate(long milliseconds) {

        // creating Date from millisecond
        Date currentDate = new Date(milliseconds);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Converting milliseconds to Date using Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return df.format(cal.getTime());

    }

    public static boolean checkDate(String today, String dateselected) {
        String d1 = today;
        String d2 = dateselected;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = false;// If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = false;// If two dates are equal
            } else {
                b = true; // If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;

    }

    public static boolean checkDateEqual(String today, String dateselected) {
        String d1 = today;
        String d2 = dateselected;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
        boolean b = false;
        try {
            if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;// If two dates are equal
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;

    }

    public static Date getTimeStamp(String inputDate) {
        Date newDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            newDate = formatter.parse(inputDate);
        } catch (ParseException e) {
        }
        return newDate;
    }



    public static String getCurrentTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String currentDateandTime = sdf.format(new Date());
        System.out.println("Time--" + currentDateandTime);
        return currentDateandTime;
    }


}



