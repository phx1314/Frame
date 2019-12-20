//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import android.annotation.SuppressLint;

import com.mdx.framework.Frame;
import com.framework.R.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verify {
    public boolean error;
    public String successmsg;
    public String errormsg;

    public Verify(boolean error, String successmsg, String errormsg) {
        this.error = error;
        this.successmsg = successmsg;
        this.errormsg = errormsg;
    }

    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof String) {
                if ((String) obj == null || ((String) obj).length() == 0) {
                    return true;
                }
            } else if ((String) obj == null || ((String) obj).length() == 0) {
                return true;
            }

            return false;
        }
    }

    public static boolean isMobile(String phone) {
        if (phone == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^13/d{9}||15[8,9]/d{8}$");
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
    }

    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public static boolean isDate(String date, String format) {
        if (date == null) {
            return false;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date d = null;

            try {
                d = df.parse(date);
            } catch (Exception var5) {
                return false;
            }

            String s1 = df.format(d);
            return date.equals(s1);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public static Verify validateIdCard(String IDStr) {
        IDStr = IDStr.toLowerCase(Locale.ENGLISH);
        String errorInfo = "";
        String[] ValCodeArr = new String[]{"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = new String[]{"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = Frame.CONTEXT.getString(string.verify_carid_num);
            return new Verify(false, "", errorInfo);
        } else {
            if (IDStr.length() == 18) {
                Ai = IDStr.substring(0, 17);
            } else if (IDStr.length() == 15) {
                Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
            }

            if (!isNumeric(Ai)) {
                errorInfo = Frame.CONTEXT.getString(string.verify_carid_lasnum);
                return new Verify(false, "", errorInfo);
            } else {
                String strYear = Ai.substring(6, 10);
                String strMonth = Ai.substring(10, 12);
                String strDay = Ai.substring(12, 14);
                if (!isDate(strYear + "-" + strMonth + "-" + strDay, "yyyy-MM-dd")) {
                    errorInfo = Frame.CONTEXT.getString(string.verify_carid_birthday);
                    return new Verify(false, "", errorInfo);
                } else {
                    GregorianCalendar gc = new GregorianCalendar();
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        if (gc.get(1) - Integer.parseInt(strYear) > 150 || gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime() < 0L) {
                            return new Verify(false, "", Frame.CONTEXT.getString(string.verify_carid_birthdayerror));
                        }
                    } catch (NumberFormatException var15) {
                        var15.printStackTrace();
                    } catch (ParseException var16) {
                        var16.printStackTrace();
                    }

                    if (Integer.parseInt(strMonth) <= 12 && Integer.parseInt(strMonth) != 0) {
                        if (Integer.parseInt(strDay) <= 31 && Integer.parseInt(strDay) != 0) {
                            Hashtable h = GetAreaCode();
                            String address = (String) h.get(Ai.substring(0, 2));
                            if (address == null) {
                                errorInfo = Frame.CONTEXT.getString(string.verify_carid_addrerror);
                                return new Verify(false, "", errorInfo);
                            } else {
                                int TotalmulAiWi = 0;

                                int modValue;
                                for (modValue = 0; modValue < 17; ++modValue) {
                                    TotalmulAiWi += Integer.parseInt(String.valueOf(Ai.charAt(modValue))) * Integer.parseInt(Wi[modValue]);
                                }

                                modValue = TotalmulAiWi % 11;
                                String strVerifyCode = ValCodeArr[modValue];
                                Ai = Ai + strVerifyCode;
                                if (IDStr.length() == 18) {
                                    if (!Ai.equals(IDStr)) {
                                        errorInfo = Frame.CONTEXT.getString(string.verify_carid_error);
                                        return new Verify(false, "", errorInfo);
                                    } else {
                                        return new Verify(true, address, errorInfo);
                                    }
                                } else {
                                    return new Verify(true, address, errorInfo);
                                }
                            }
                        } else {
                            errorInfo = Frame.CONTEXT.getString(string.verify_carid_dayerror);
                            return new Verify(false, "", errorInfo);
                        }
                    } else {
                        errorInfo = Frame.CONTEXT.getString(string.verify_carid_montherror);
                        return new Verify(false, "", errorInfo);
                    }
                }
            }
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    private static Hashtable<String, String> GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", Frame.CONTEXT.getString(string.verify_carid_addres_11));
        hashtable.put("12", Frame.CONTEXT.getString(string.verify_carid_addres_12));
        hashtable.put("13", Frame.CONTEXT.getString(string.verify_carid_addres_13));
        hashtable.put("14", Frame.CONTEXT.getString(string.verify_carid_addres_14));
        hashtable.put("15", Frame.CONTEXT.getString(string.verify_carid_addres_15));
        hashtable.put("21", Frame.CONTEXT.getString(string.verify_carid_addres_21));
        hashtable.put("22", Frame.CONTEXT.getString(string.verify_carid_addres_22));
        hashtable.put("23", Frame.CONTEXT.getString(string.verify_carid_addres_23));
        hashtable.put("31", Frame.CONTEXT.getString(string.verify_carid_addres_31));
        hashtable.put("32", Frame.CONTEXT.getString(string.verify_carid_addres_32));
        hashtable.put("33", Frame.CONTEXT.getString(string.verify_carid_addres_33));
        hashtable.put("34", Frame.CONTEXT.getString(string.verify_carid_addres_34));
        hashtable.put("35", Frame.CONTEXT.getString(string.verify_carid_addres_35));
        hashtable.put("36", Frame.CONTEXT.getString(string.verify_carid_addres_36));
        hashtable.put("37", Frame.CONTEXT.getString(string.verify_carid_addres_37));
        hashtable.put("41", Frame.CONTEXT.getString(string.verify_carid_addres_41));
        hashtable.put("42", Frame.CONTEXT.getString(string.verify_carid_addres_42));
        hashtable.put("43", Frame.CONTEXT.getString(string.verify_carid_addres_43));
        hashtable.put("44", Frame.CONTEXT.getString(string.verify_carid_addres_44));
        hashtable.put("45", Frame.CONTEXT.getString(string.verify_carid_addres_45));
        hashtable.put("46", Frame.CONTEXT.getString(string.verify_carid_addres_46));
        hashtable.put("50", Frame.CONTEXT.getString(string.verify_carid_addres_50));
        hashtable.put("51", Frame.CONTEXT.getString(string.verify_carid_addres_51));
        hashtable.put("52", Frame.CONTEXT.getString(string.verify_carid_addres_52));
        hashtable.put("53", Frame.CONTEXT.getString(string.verify_carid_addres_53));
        hashtable.put("54", Frame.CONTEXT.getString(string.verify_carid_addres_54));
        hashtable.put("61", Frame.CONTEXT.getString(string.verify_carid_addres_61));
        hashtable.put("62", Frame.CONTEXT.getString(string.verify_carid_addres_62));
        hashtable.put("63", Frame.CONTEXT.getString(string.verify_carid_addres_63));
        hashtable.put("64", Frame.CONTEXT.getString(string.verify_carid_addres_64));
        hashtable.put("65", Frame.CONTEXT.getString(string.verify_carid_addres_65));
        hashtable.put("71", Frame.CONTEXT.getString(string.verify_carid_addres_71));
        hashtable.put("81", Frame.CONTEXT.getString(string.verify_carid_addres_81));
        hashtable.put("82", Frame.CONTEXT.getString(string.verify_carid_addres_82));
        hashtable.put("91", Frame.CONTEXT.getString(string.verify_carid_addres_91));
        return hashtable;
    }
}
