package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;

import com.kenzie.capstone.service.dao.UserAccountsDao;

import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
        includes = DaoModule.class
)
public class ServiceModule {

    /*  @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("ExampleDao") ExampleDao exampleDao) {
        return new LambdaService(exampleDao);
    }

   */

    // (UserAccounts Lambda. Added table and stack to aws. Update the lambda files for user account usage.
    // Still need to test. still need to implemnt within our application service after testing is finish.)

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("UserAccountsDao") UserAccountsDao userAccountsDao) {
        return new LambdaService(userAccountsDao);
    }
}

