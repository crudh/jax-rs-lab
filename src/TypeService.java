import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/")
public class TypeService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String doTypeTest(All input) {
        System.out.println("TYPE: " + input.getType());

        switch (Type.valueOf(input.getType())) {
            case one:
                One one = (One)input;
                return "One: " + one.getType() + " - " + one;
            case two:
                Two two = (Two)input;
                return "Two: " + two.getType() + " - " + two;
            case three:
                Three three = (Three)input;
                return "Three: " + three.getType() + " - " + three;
        }

        return "No match";
    }

    public static enum Type {
        one, two, three;
    }

    public static class All implements Base, One, Two, Three {
        private String type;
        private String one;
        private String two;
        private String three;

        @Override
        public String getType() {
            return type;
        }

        @Override
        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String getOne() {
            return one;
        }

        @Override
        public String getThree() {
            return two;
        }

        @Override
        public String getTwo() {
            return three;
        }

        @Override
        public void setOne(String one) {
            this.one = one;
        }

        @Override
        public void setTwo(String two) {
            this.two = two;
        }

        @Override
        public void setThree(String three) {
            this.three = three;
        }

        @Override
        public String toString() {
            return "{" +
                    "type='" + type + '\'' +
                    ", one='" + one + '\'' +
                    ", two='" + two + '\'' +
                    ", three='" + three + '\'' +
                    '}';
        }
    }

    public interface Base {
        String getType();
        void setType(String type);
    }

    public interface One extends Base {
        String getOne();
        void setOne(String one);
    }

    public interface Two extends Base {
        String getTwo();
        void setTwo(String two);
    }

    public interface Three extends Base {
        String getThree();
        void setThree(String three);
    }
}
