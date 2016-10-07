package com.one.test.testappone;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBanner = (TextView) findViewById(R.id.textView);
        equationText = (EditText) findViewById(R.id.equation);
        buttonSolve = (Button) findViewById(R.id.button);
        answerText = (TextView) findViewById(R.id.answer);
        equationEnter = (TextView) findViewById(R.id.textView2);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontThree.ttf");
        mainBanner.setTypeface(font);
        equationEnter.setTypeface(font);
        answerText.setTypeface(font);

    }

    static String answerFinal = "";
    static String answerOne = "";
    static String thisRightHere = "lol";

    TextView mainBanner;
    TextView equationEnter;
    EditText equationText;
    Button buttonSolve;
    TextView answerText;

    public static ArrayList<String> SingleTermOrNah(String input)
    {
        //first method initiated if chain. Checks if there are seperable terms, and how many.
        ArrayList<Integer> locations = new ArrayList<>();
        ArrayList<String> terms = new ArrayList<>();
        int lel = 0;
        int numberOfTerms = 1;
        for (int a = 0; a<input.length(); a++)
        {
            if (input.charAt(a) == "(".charAt(0))
            {
                lel++;
            }
            if (input.charAt(a) == ")".charAt(0))
            {
                lel--;
            }
            if (((input.charAt(a) == "+".charAt(0)&& input.charAt(a-1) != "^".charAt(0))
                    || input.charAt(a) == "-".charAt(0) && input.charAt(a-1) != "^".charAt(0)) && lel==0)
            {
                //System.out.println("found " + a);
                numberOfTerms++;
                locations.add(a);
                //terms.add(Character.toString((input.charAt(locations.get(a)))));
            }
        }

        //System.out.println("a is " + locations);
        int locationSize = locations.size();
        if (numberOfTerms == 1)
        {
            terms.add(input);
        }
        else if (numberOfTerms != 1)
        {
            for (int a = 0; a < locationSize; a++)
            {
                if (a == 0)
                {
                    terms.add(input.substring(0, locations.get(a)));
                }
                else if (a != 0)
                {
                    terms.add(input.substring(locations.get(a-1)+1, locations.get(a)));
                }
            }
            terms.add(input.substring(locations.get(locationSize-1)+1, input.length()));
        }

        for (int a = 0; a < locationSize; a++)
        {
            terms.add(Character.toString(input.charAt(locations.get(a))));
        }
        return terms;
    }

    public static String NoChainSolver(String input)
    {
        String answer = "";
        //System.out.println("NoChainSolver");

        String power = "";
        String coeff = "";

        int length = input.length();
        //System.out.println("length is " + length);

        //start from the end to find ^
        //System.out.println("finding last ^");
        for (int a = length-1; a >= 0; a--)
        {
            Character single = input.charAt(a);
            if (single == "^".charAt(0))
            {
                //System.out.println("power symbol ^ at " + a);
                //acquired power symbol.
                //find if power is for x or another equation i.e. (fx).
                for (int b = a-1; b >= 0; b--)
                {
                    Character album = input.charAt(b);

                    if (album == "x".charAt(0))
                    {
                        //System.out.println("power is for x");
                        //System.out.println("x at " + b);
                        //left is x. x is at b.

                        //To solve, first find power.
                        for (int c = b+2; c < length; c++)
                        {
                            if (input.charAt(c) == "+".charAt(0) || input.charAt(c) == "*".charAt(0))
                            {
                                break;
                            }

                            else
                            {
                                power = power + input.charAt(c);
                            }
                        }
                        //System.out.println("power is " + power);
                        //power acquired.
                        //next find coefficient
                        for (int c = b-1; c >= 0; c--)
                        {
                            if (c == 0)//no term seperator before b (x position)
                            {
                                for (int d = 0; d < b; d++)
                                {
                                    coeff = coeff + input.charAt(d);
                                }
                            }

                            if (input.charAt(c) == "+".charAt(0) || input.charAt(c) == "*".charAt(0)
                                    || input.charAt(c) == "(".charAt(0) || input.charAt(c) == ")".charAt(0))
                            {
                                //found term seperator at c
                                for (int d = c+1; d<b; d++)//coefficient occurs after c and before b (x position)
                                {
                                    coeff = coeff + input.charAt(d);
                                }
                                //System.out.println("coefflol");
                                break;//remove break, instead do something about other terms. If * and if +.
                                //get position of term seperator and start process <- from there.
                            }
                        }
                        //System.out.println("coeff is " + coeff);
                        //power and coeff acquired.
                        //next, solve
                        //System.out.println(coeff + "coeff");
                        //System.out.println(power + "power");
                        answerOne = answerOne + Double.parseDouble(power)*Double.parseDouble(coeff) + "x^" + (Double.parseDouble(power)-1);
                        //System.out.println(answerOne);
                        answerFinal = answerOne + " + " + answerFinal;
                        answerOne = "";
                        coeff = "";
                        power = "";
                        //System.out.println("reset");
                    }

                    else if (album == ")".charAt(0))
                    {
                        System.out.println("error");
                        //this situation shall never be true(ideally). Hence error.
                        //left is ).
                        //leftFinderBracket
                    }
                    break;
                }
            }
        }

        answerFinal = answerFinal.trim();
        int lel = answerFinal.length();
        if (answerFinal.charAt(lel-1) == "+".charAt(0) || answerFinal.charAt(lel-1) == "-".charAt(0))
        {
            answerFinal = answerFinal.substring(0, (lel-1));
        }
        answerFinal = answerFinal.trim();

        //System.out.println("final answer is " + answerFinal);
        answer = answerFinal;
        answerFinal = "";
        return answer;
    }

    public static String Solver(String input)
    {
        if (ChainOrNoChain(input))
        {
            boolean deeper = false;
            int powerPos = 0;
            String exp = "";
            String acTerm = "";

            //the power symbol.
            for (int a = 0; a < input.length(); a++)
            {
                if (input.charAt(a) == "^".charAt(0))
                {
                    powerPos = a;
                }
            }
            //!The power symbol.

            //the Term's exponent.
            for (int a = powerPos+1; a < input.length(); a++)
            {
                exp = exp + input.charAt(a);
            }
            //System.out.println(exp);
            //!The term's exponent.

            //The actual term considered.
            int start = 0;
            int end = 0;
            for (int a = 0; a < input.length(); a++)
            {
                if (input.charAt(a) == "(".charAt(0))
                {
                    start = a;
                    break;
                }
            }

            for (int a = input.length() - 1; a >= 0; a--)
            {
                if (input.charAt(a) == ")".charAt(0))
                {
                    end = a;
                    break;
                }
            }

            acTerm = input.substring(start, end+1);
            //System.out.println(acTerm + "this should be the acTerm");
            //!The term considered.

            //The required job.
            String answer = "";
            answer = answer + exp + acTerm + "^(" + (Float.valueOf(exp)-1) + ")";

            //is deeper required?
            for (int a = 0; a < acTerm.length(); a++)
            {
                if(acTerm.charAt(a) == "x".charAt(0))
                {
                    deeper = true;
                }
            }

            if (deeper)
            {
                return "(" + answer + ")*" + GoDeep(acTerm);
            }
            else
            {
                return answer;
            }

        }

        else
        {
            //System.out.println();
            //System.out.println("ayy");
            //System.out.println();
            return NoChainSolver(input);
        }
    }

    public static Boolean ChainOrNoChain(String input)
    {
        for (int a = (input.length())-1; a >= 0; a--)
        {
            if (input.charAt(a) == ")".charAt(0))
            {
                return true;
            }
        }
        return false;
    }

    public static String GoDeep(String input)
    {
        String answer = "";
        input = input.substring(1);
        //System.out.println(input + "lets see");
        input = input.substring(0, input.length()-1);
        //System.out.println(input);

        answer = Solver(input);
        return "(" + answer + ")";
    }

    public String MasterSolver(String inputOne)
    {
        ArrayList<String> input = SingleTermOrNah(inputOne);
        //System.out.println(input + "here is the arraylist");
        String answerHere = "";
        int seperatorIndex = 0;
        int seperator = 0;

        if (input.size() == 1)
        {
            answerHere = Solver(input.get(0));
        }

        else if (input.size() != 1)
        {
            for (int a = 0; a < input.size(); a++)
            {
                if (input.get(a).charAt(0) == "+".charAt(0) || input.get(a).charAt(0) == "-".charAt(0))
                {
                    seperatorIndex = a;
                    seperator = a;
                    //System.out.println("as of now, seperatorIndex and seperator are " + a);
                    break;
                }
            }

            for (int a = 0; a < seperatorIndex; a++)
            {
                answerHere = answerHere + Solver(input.get(a));
                if (a != seperatorIndex -1)
                {
                    answerHere = answerHere + " " + input.get(seperator) + " ";
                    seperator++;
                }

            }
        }

        //System.out.println();
        //System.out.println(answer);
        return answerHere;
    }

    public void listenerOne(View view)
    {
        Log.d("CREATION", "ayy");
        Log.d("CREATION", String.valueOf(equationText.getText()));
        Log.d("CREATION", MasterSolver(String.valueOf(equationText.getText())));
        thisRightHere = MasterSolver(String.valueOf(equationText.getText()));
        answerText.setText(thisRightHere);
    }
}