import java.util.ArrayList;

public class Constraint<A> {

    public boolean prePost;
    public int[] variables;
    public Lambda<A> lambda;
    public Constraint(boolean prePost, int[] variables, Lambda<A> lambda) {
        this.prePost = prePost;
        this.variables = variables;
        this.lambda = lambda;
    }
}
