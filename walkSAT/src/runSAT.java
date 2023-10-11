import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.*;

public class runSAT {
    public static void main(String[] args) {
        int fixed_variables = 20;
        List<Integer> terminations = new ArrayList<>();
        List<Integer> flips = new ArrayList<>();
        List<Integer> x_axis = new ArrayList<>();
        ArrayList<Literal> constant_20_literal_array = create_20_literals(fixed_variables); //A total of 40 literals
        WalkSATWrite walkSAT = new WalkSATWrite();
        //For every iteration:
        for(int i = 1; i <= 10; i ++) {
            int successful_terminations = 0;
            int total_flips = 0;
            x_axis.add(i);
            System.out.println("We are at C/N: "+ i);
            for(int j = 0; j <= 50; j ++) {
                Set<Clause> clauseSet = generate_n_rand_3SAT(constant_20_literal_array, fixed_variables*i);
                List<Object> model_number_pair = walkSAT.walkSAT(clauseSet, 0.5, -1);
                if( model_number_pair.get(0) != null) {
                    successful_terminations++;
                }
                total_flips += (int)model_number_pair.get(1);
            }
            terminations.add(successful_terminations);
            flips.add(total_flips / 50);
        }
        //Create plot
        Plot plt = Plot.create();
        plt.plot().add(x_axis, terminations).color("blue");
        plt.plot().add(x_axis, flips).color("red");
        try {
            plt.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }

    }



    //HELPERS:
    public static ArrayList<Literal> create_20_literals(int number_of_literals){
        ArrayList<Literal> array_of_literals = new ArrayList<>();
        for(int i = 0; i < number_of_literals; i++){
            PropositionSymbol ps = new PropositionSymbol("p"+ (i + 1));
            Literal l = new Literal(ps);
            Literal negate_l = new Literal(ps, false);
            array_of_literals.add(l);
            array_of_literals.add(negate_l);
        }
        return array_of_literals;
    }

    public static Literal get_random_literal(List<Literal> s) {
        Random rand = new Random();
        return s.get(rand.nextInt(s.size()));
    }
    //Creates random 3SAT with removal
    public static Clause create_random_3SAT_clause(ArrayList<Literal> array_of_literals){
        List<Literal> array_copy = new ArrayList<Literal>(array_of_literals); //Clone the array of literals
        ArrayList<Literal>  list_of_3sat = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Literal random_literal = get_random_literal(array_copy);
            list_of_3sat.add(random_literal);
            array_copy.remove(random_literal);
        }
        Clause clauses_3sat = new Clause(list_of_3sat);
        return clauses_3sat;
    }

    public static Set<Clause> generate_n_rand_3SAT(ArrayList<Literal> array_of_literals, int n){
        Set<Clause> clauseSet = new HashSet<Clause>();
        //Iterate over this 50 times:
        for(int i = 0; i < n; i++){
            Clause current_random_3SAT_clause = create_random_3SAT_clause(array_of_literals);
            clauseSet.add(current_random_3SAT_clause);
        }
        return clauseSet;
    }

}
