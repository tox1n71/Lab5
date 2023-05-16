package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.worker.Organization;

import java.io.File;

public class FilterLessThanOrganizationCommand implements Command{
        private String name = "filter_less_than_organization";
        private CollectionManager cm;
        private Organization organization;
        public FilterLessThanOrganizationCommand(Organization organization){
            this.organization = organization;
        }

        @Override
        public String execute(){
            return cm.filterLessThanOrganization(organization);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setCollectionManager(CollectionManager cm) {
            this.cm = cm;
        }

}