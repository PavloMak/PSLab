package ua.kpi.controllers;

public class Cripto {
    public static String encryption(String str, int k) {
        String string="";
        for(int i=0;i<str.length();i++) {
            char c=str.charAt(i);
            if(c>='a'&&c<='z')
            {
                c+=k%26;
                if(c<'a')
                    c+=26;
                if(c>'z')
                    c-=26;
            }else if(c>='A'&&c<='Z')
            {
                c+=k%26;
                if(c<'A')
                    c+=26;
                if(c>'Z')
                    c-=26;
            }
            string += c;
        }
        return string;
    }



    public static String decrypt(String str, int n) {
        int k=Integer.parseInt("-"+n);
        String string="";
        for(int i=0;i<str.length();i++) {
            char c=str.charAt(i);
            if(c>='a'&&c<='z')
            {
                c+=k%26;
                if(c<'a')
                    c+=26;
                if(c>'z')
                    c-=26;
            }else if(c>='A'&&c<='Z')
            {
                c+=k%26;
                if(c<'A')
                    c+=26;
                if(c>'Z')
                    c-=26;
            }
            string += c;
        }
        return string;
    }

}

