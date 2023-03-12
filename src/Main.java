import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Sample usage
        ArrayList<String[]> domain = new ArrayList<>();
        domain.add(new String[] {"0", "1"});
        domain.add(new String[] {"0", "1"});
        domain.add(new String[] {"0", "1"});

        Solver<String> solve = new Solver<>();
        ArrayList<Constraint<String>> constraints = new ArrayList<>();

        List<String> output;
        do {
            List<String> out = solve.solve(domain, constraints);
            output = out;
            if (out != null){
                for (String str : out) {
                    System.out.print(str);
                }
                System.out.println("");
                Lambda<String> lambda = (x) -> !(x.get(0) == out.get(0) && x.get(1) == out.get(1) && x.get(2) == out.get(2));
                constraints.add(new Constraint<>(true, null, lambda));
            }

        } while (output != null);
    }

}
