package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.services.UserService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICPTCodeTest {
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CPTCodeService        service;

    @Autowired
    private UserService<User>     userService;

    /**
     * Sets up test
     */
    @Before
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void setup () throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
        userService.deleteAll();

        userService.save( new Personnel( new UserForm( "admin", "admin", Role.ROLE_ADMIN, 1 ) ) );
        // userService.save( new Personnel( new UserForm( "hcp", "hcp",
        // Role.ROLE_HCP, 1 ) ) );
        userService.save( new Personnel( new UserForm( "billing", "billing", Role.ROLE_BILLING_STAFF, 1 ) ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "billing", roles = { "BILLING_STAFF" } )
    public void testCodeAPI () throws Exception {
        service.deleteAll();
        final CPTCodeForm form = new CPTCodeForm();
        form.setCode( "99202" );
        form.setDescription( "15-29 Minutes" );
        form.setCost( 10.20 );
        form.setTimeFrame( "15-29 minutes" );

        final CPTCode c = new CPTCode( form );
        c.setCode( "99202" );

        String content = mvc.perform( post( "/api/v1/cptcodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        CPTCode code = TestUtils.gson().fromJson( content, CPTCode.class );

        assertEquals( c.getCode(), code.getCode() );

        content = mvc.perform( get( "/api/v1/cptcode/" ).contentType( MediaType.APPLICATION_JSON ) ).andReturn()
                .getResponse().getContentAsString();
        // code = TestUtils.gson().fromJson( content, CPTCodeForm.class );
        assertEquals( form.getCode(), code.getCode() );

        // edit it
        form.setCode( "99203" );
        final CPTCode c2 = new CPTCode( form );
        c2.setCode( "99203" );
        content = mvc.perform( put( "/api/v1/cptcodes/" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCode.class );
        assertEquals( form.getCode(), c2.getCode() );
        content = mvc.perform( get( "/api/v1/cptcode/" ).contentType( MediaType.APPLICATION_JSON ) ).andReturn()
                .getResponse().getContentAsString();
        code = TestUtils.gson().fromJson( content, CPTCode.class );
        assertEquals( form.getCode(), c2.getCode() );

    }
}
