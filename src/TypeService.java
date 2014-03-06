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
    @Produces(MediaType.APPLICATION_JSON)
    public All doTypeTest(All input) {
        System.out.println("TYPE: " + input.getType());

        switch (Type.valueOf(input.getType())) {
            case one:
                System.out.println("ONE: " + ((One)input));
                break;
            case two:
                System.out.println("TWO: " + ((Two)input));
                break;
            case three:
                System.out.println("THREE: " + ((Three)input));
                break;
        }

        return input;
    }

    public static enum Type {
        one, two, three;
    }

    public static class All implements One, Two, Three {
        private String type;
        private String one;
        private String two;
        private String three;

        public String getType() {
            return type;
        }

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

    public interface One {
        String getOne();
        void setOne(String one);
    }

    public interface Two {
        String getTwo();
        void setTwo(String two);
    }

    public interface Three {
        String getThree();
        void setThree(String three);
    }
}
