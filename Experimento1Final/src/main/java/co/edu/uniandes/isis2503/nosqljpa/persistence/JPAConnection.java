package co.edu.uniandes.isis2503.nosqljpa.persistence;

import com.impetus.client.cassandra.common.CassandraConstants;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAConnection {

    public static final String CASSANDRA = "cassandraExp1_db";
    public static final String DERBY = "derbyExp1_db";

    public static final String DB = DERBY;

    private EntityManager entityManager;
    public static final JPAConnection CONNECTION = new JPAConnection();

    public JPAConnection() {
        if (entityManager == null) {
            EntityManagerFactory emf;
            if (DB.equals(DERBY)) {
                emf = Persistence.createEntityManagerFactory(DERBY);
                System.out.println("SE LOGRO");
            } else {
                Map<String, String> propertyMap = new HashMap<>();
                propertyMap.put(CassandraConstants.CQL_VERSION, CassandraConstants.CQL_VERSION_3_0);
                emf = Persistence.createEntityManagerFactory(CASSANDRA, propertyMap);
            }
            entityManager = emf.createEntityManager();
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
