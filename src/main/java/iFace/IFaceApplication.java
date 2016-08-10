package iFace;

import iFace.dao.CommunityDAO;
import iFace.dao.UserDAO;
import iFace.model.User;
import iFace.resources.CommunityResource;
import iFace.resources.UserResource;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.joda.time.DateTimeZone;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class IFaceApplication extends io.dropwizard.Application<IFaceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IFaceApplication.class);
    private HibernateBundle<IFaceConfiguration> hibernate = new HibernateBundle<IFaceConfiguration>(User.class, getEntities()) {
        @Override
        public DataSourceFactory getDataSourceFactory(IFaceConfiguration configuration) {
            return configuration.getDatabase();
        }
    };

    public static void main(String[] args) throws Exception {
        new IFaceApplication().run(new String[]{"server", "config.yaml"});
    }

    private Class[] getEntities() { //<-- Isso aqui é para o hibernate procurar todas as classes que tem no pacote modelo, para ele criar as tabelas
        List<ClassLoader> classLoadersList = new LinkedList();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner(), new TypeAnnotationsScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("iFace.model"))));

        Set<Class<? extends Object>> allClasses
                = reflections.getTypesAnnotatedWith(Entity.class);
        allClasses.remove(User.class);
        return allClasses.toArray(new Class[allClasses.size()]);
    }

    @Override
    public void initialize(Bootstrap<IFaceConfiguration> b) {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        b.addBundle(hibernate);
    }

    @Override
    public void run(IFaceConfiguration c, Environment e) throws Exception {

        final FilterRegistration.Dynamic cors
                = e.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        LOGGER.info("Method Application#run() called");

        //Isso aqui é para dizer que tem serviços na classe UserResource e ele utilizará o dao UserDAO
        final UserDAO userDao = new UserDAO(hibernate.getSessionFactory());
        final CommunityDAO communityDAO = new CommunityDAO(hibernate.getSessionFactory());

        e.jersey().register(new UserResource(userDao));
        e.jersey().register(new CommunityResource(communityDAO));

    }

}
