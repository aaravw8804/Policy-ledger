package com.hdfclife.ledger.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hdfclife.ledger.domain.Customer;
import com.hdfclife.ledger.domain.Policy;
import com.hdfclife.ledger.domain.Rider;
import com.hdfclife.ledger.repo.CustomerRepository;
import com.hdfclife.ledger.repo.PolicyRepository;
import com.hdfclife.ledger.repo.RiderRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final RiderRepository riderRepository;
    private final Environment environment;

    public DataSeeder(CustomerRepository customerRepository,
                       PolicyRepository policyRepository,
                       RiderRepository riderRepository,
                       Environment environment) {
        this.customerRepository = customerRepository;
        this.policyRepository = policyRepository;
        this.riderRepository = riderRepository;
        this.environment = environment;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (policyRepository.count() == 0) {
            seed();
        }
        printStartupSummary();
    }

    private void seed() {
        Map<String, Customer> customersByName = new LinkedHashMap<>();
        customersByName.put("Anita Sharma", customerRepository.save(new Customer("Anita Sharma", "anita.sharma@hdfclife.example")));
        customersByName.put("Rahul Mehta", customerRepository.save(new Customer("Rahul Mehta", "rahul.mehta@hdfclife.example")));
        customersByName.put("Priya Nair", customerRepository.save(new Customer("Priya Nair", "priya.nair@hdfclife.example")));
        customersByName.put("Vikram Singh", customerRepository.save(new Customer("Vikram Singh", "vikram.singh@hdfclife.example")));
        customersByName.put("Sneha Patel", customerRepository.save(new Customer("Sneha Patel", "sneha.patel@hdfclife.example")));

        policyRepository.save(new Policy("HDFC-LIFE-1001", customersByName.get("Anita Sharma"), "TERM", 18500, "Active"));
        policyRepository.save(new Policy("HDFC-LIFE-1002", customersByName.get("Rahul Mehta"), "ULIP", 42000, "Active"));
        policyRepository.save(new Policy("HDFC-LIFE-1003", customersByName.get("Priya Nair"), "ENDOWMENT", 27000, "Lapsed"));
        policyRepository.save(new Policy("HDFC-LIFE-1004", customersByName.get("Vikram Singh"), "TERM", 15200, "Active"));
        policyRepository.save(new Policy("HDFC-LIFE-1005", customersByName.get("Sneha Patel"), "ULIP", 36000, "Active"));
        policyRepository.save(new Policy("HDFC-LIFE-1006", customersByName.get("Anita Sharma"), "ENDOWMENT", 22000, "Pending"));

        Policy policy1001 = policyRepository.findByPolicyNo("HDFC-LIFE-1001").orElseThrow();
        Rider accidentCover = riderRepository.findByCode("ACCIDENT_COVER").orElseThrow();
        Rider waiverOfPremium = riderRepository.findByCode("WAIVER_OF_PREMIUM").orElseThrow();
        policy1001.addRider(accidentCover);
        policy1001.addRider(waiverOfPremium);
        policyRepository.save(policy1001);
    }

    private void printStartupSummary() {
        String[] activeProfiles = environment.getActiveProfiles();
        String profile = activeProfiles.length > 0 ? activeProfiles[0] : "dev";

        System.out.println("Active profile -> " + profile);
        System.out.println("Policy count -> " + policyRepository.count());
        System.out.println("Customer count -> " + customerRepository.count());

        policyRepository.findByPolicyNo("HDFC-LIFE-1004").ifPresent(p ->
                System.out.println("Lookup HDFC-LIFE-1004 customer -> " + p.getCustomer().getFullName()));

        System.out.println("Active count -> " + policyRepository.findByStatusOrderByPolicyNoAsc("Active").size());
        System.out.println("TERM count -> " + policyRepository.findByProductTypeOrderByPolicyNoAsc("TERM").size());
        System.out.println("Anita Sharma policy count -> "
                + policyRepository.findByCustomer_FullNameOrderByPolicyNoAsc("Anita Sharma").size());

        String minPremiumPolicyNumbers = policyRepository.findWithPremiumAtLeast(20000).stream()
                .map(Policy::getPolicyNo)
                .collect(Collectors.joining(", "));
        System.out.println("minPremium=20000 policy numbers -> " + minPremiumPolicyNumbers);

        policyRepository.findByPolicyNo("HDFC-LIFE-1001").ifPresent(p -> {
            String riderCodes = p.getRiders().stream()
                    .map(Rider::getCode)
                    .sorted()
                    .collect(Collectors.joining(", "));
            System.out.println("HDFC-LIFE-1001 rider codes -> " + riderCodes);
        });
    }
}
