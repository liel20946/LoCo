package com.example.user.loco;


public class Colors {


    public static void ChangeAccent(String ac) {

        int acccInt;
        TinyDB tdb = new TinyDB(MainActivity.cpn);

        switch (ac) {
            case "r":
                acccInt = R.color.Red;

                break;
            case "g":
                acccInt = R.color.green;

                break;
            case "p":
                acccInt = R.color.purple;

                break;

            case "y":
                acccInt = R.color.yellow;

                break;

            case "c":
                acccInt = R.color.cyan;

                break;

            case "b":
                acccInt = R.color.blue;

                break;

            case "gg":
                acccInt = R.color.lightgreen;

                break;

            case "pp":
                acccInt = R.color.pink;

                break;

            default:
                acccInt = R.color.LoCoOrange;


        }

        tdb.putInt("colorAc", acccInt);


    }

}
