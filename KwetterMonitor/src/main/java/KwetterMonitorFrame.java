import javax.jms.*;
import javax.jms.MessageListener;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Niek on 07/04/14.
 * © Aidas 2014
 */
public class KwetterMonitorFrame
{
    private JLabel lblTopic;
    private JPanel formPanel;
    private JComboBox comboBox1;
    private JButton btnSubscribe;
    private JList list1;

    private final static String SPORTS_TOPIC = "Sports";
    private final static String POLITICS_TOPIC = "Politics";
    private final static String GAMES_TOPIC = "Games";
    private final static String COMPUTERS_TOPIC = "Computers";

    /**
     * the preconfigured GlassFish-default connection factory
     */
    private static final String JNDI_CONNECTION_FACTORY = "jms/__defaultConnectionFactory";
    /**
     * the JNDI name under which your {@link javax.jms.Topic} should be: you have to
     * create the topic before running this class
     */
    private static final String JNDI_TOPIC = "KwetterMonitorTopic";

    private static ConnectionFactory connectionFactory;
    private static Topic topic;

    private static JMSContext context;

    private JMSConsumer currentConsumer = null;

    public KwetterMonitorFrame()
    {
        this.comboBox1.addItem(SPORTS_TOPIC);
        this.comboBox1.addItem(POLITICS_TOPIC);
        this.comboBox1.addItem(GAMES_TOPIC);
        this.comboBox1.addItem(COMPUTERS_TOPIC);

        btnSubscribe.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String selectedTopic = (String) comboBox1.getSelectedItem();

                final DefaultListModel listModel = new DefaultListModel();
                list1.setModel(listModel);

                if (currentConsumer != null)
                {
                    currentConsumer.close();
                    currentConsumer = null;
                }

                currentConsumer = context.createConsumer(topic, "topicType = '"+selectedTopic+"'");
                currentConsumer.setMessageListener(new MessageListener()
                {
                    @Override
                    public void onMessage(Message message)
                    {
                        System.out.println("Received message");
                        try
                        {
                            String content = message.getStringProperty("content");
                            String user = message.getStringProperty("postedBy");
                            String postedAt = message.getStringProperty("postedAt");
                            listModel.addElement(content + " - by " + user + " @ " + postedAt);
                            list1.setModel(listModel);
                            list1.setSelectedIndex(0);
                        } catch (JMSException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public static void main(String[] args)
    {
        connectionFactory = lookup(ConnectionFactory.class, JNDI_CONNECTION_FACTORY);
        topic = lookup(Topic.class, JNDI_TOPIC);

        context = connectionFactory.createContext();

        JFrame frame = new JFrame("KwetterMonitorFrame");
        frame.setContentPane(new KwetterMonitorFrame().formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 500);
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
