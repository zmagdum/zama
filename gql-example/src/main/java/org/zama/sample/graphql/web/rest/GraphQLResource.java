package org.zama.sample.graphql.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.zama.sample.graphql.fields.GraphQLExecutor;
import org.zama.sample.graphql.web.rest.vm.LoggerVM;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/graphql")
public class GraphQLResource {
    @Autowired
    private GraphQLExecutor executor;

    @PostMapping("/")
    @Transactional
    @Timed
    public Object executeRequest(@RequestBody String query) {
        return executor.executeRequest(query);
    }
}
git
