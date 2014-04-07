import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by Niek on 06/04/14.
 * © Aidas 2014
 */
public class MainForm
{

    private JPanel panelField;
    private JTextArea textArea1;
    private JTextField txtUser;
    private JButton postButton;


    private static final Logger LOG = Logger.getLogger(MainForm.class.getName());
    /**
     * the preconfigured GlassFish-default connection factory
     */
    private static final String JNDI_CONNECTION_FACTORY = "jms/__defaultConnectionFactory";
    /**
     * the JNDI name under which your {@link javax.jms.Topic} should be: you have to
     * create the topic before running this class
     */
    private static final String JNDI_TOPIC = "KwetterGoQueue";

    private static ConnectionFactory connectionFactory;
    private static Queue queue;



    public MainForm()
    {
        postButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Post clicked");

                String username = txtUser.getText();
                String tweetContent = textArea1.getText();

                if (!username.isEmpty() && !tweetContent.isEmpty())
                {
                    sendMessage(username, tweetContent);
                    JOptionPane.showMessageDialog(null, "Message sent!");
                    textArea1.setText("");
                    txtUser.setText("");

                }
            }
        });
    }

    private void sendMessage(String username, String contents)
    {
        //JMSContext implements AutoClosable: let us try 'try-with-resources'
        //see http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (JMSContext jmsContext = connectionFactory.createContext())
        {
            final JMSProducer producer = jmsContext.createProducer();

            Message message = jmsContext.createObjectMessage();
            message.setStringProperty("username", username);
            message.setStringProperty("content", contents);

            producer.send(queue, message);
        } catch(JMSException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        connectionFactory = lookup(ConnectionFactory.class, JNDI_CONNECTION_FACTORY);
        queue = lookup(Queue.class, JNDI_TOPIC);

        JFrame frame = new JFrame("Kwetter GO");
        frame.setContentPane(new MainForm().panelField);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
    /**
     * @param <T>         the return type
     * @param retvalClass the returned value's {@link Class}
     * @param jndi        the JNDI path to the resource
     * @return the resource at the specified {@code jndi} path
     */
    private static <T> T lookup(Class<T> retvalClass, String jndi)
    {
        try
        {

            return retvalClass.cast(InitialContext.doLookup(jndi));
        } catch (NamingException ex)
        {
            throw new IllegalArgumentException("failed to lookup instance of " + retvalClass + " at " + jndi, ex);
        }
    }


}
