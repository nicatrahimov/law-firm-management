package az.coders.lawfirmmanagement.service.Impl;

import az.coders.lawfirmmanagement.dto.CompanyDto;
import az.coders.lawfirmmanagement.exception.CompanyNotFound;
import az.coders.lawfirmmanagement.model.Company;
import az.coders.lawfirmmanagement.repository.CompanyRepository;
import az.coders.lawfirmmanagement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company>companies=companyRepository.findAll();
//        List<CompanyDto>dtos=new ArrayList<>();

         List<CompanyDto> dtos1 = companies.stream().map(x -> modelMapper.map(x, CompanyDto.class)).toList();

return dtos1;
//        for(Company c:
//        companies){
//            CompanyDto dto = CompanyDto.builder()
//                    .name(c.getName())
//                    .city(c.getCity())
//                    .email(c.getEmail())
//                    .website(c.getWebsite())
//                    .address(c.getAddress())
//                    .description(c.getDescription())
//                    .country(c.getCountry())
//                    .phoneNumber(c.getPhoneNumber())
//                    .id(c.getId())
//                    .build();
//        }

    }

    @Override
    public CompanyDto getCompById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(RuntimeException::new);
        return modelMapper.map(company, CompanyDto.class);

    }

    @Override
    public String addCompany(CompanyDto companyDto) {
        Company company = Company.builder()
                .email(companyDto.getEmail())
                .address(companyDto.getAddress())
                .city(companyDto.getCity())
                .name(companyDto.getName())
                .phoneNumber(companyDto.getPhoneNumber())
                .country(companyDto.getCountry())
                .description(companyDto.getDescription())
                .website(companyDto.getWebsite())
                .build();
        companyRepository.save(company);
        return "Successfully";
    }

    @Override
    public String deleteCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(RuntimeException::new);
        companyRepository.delete(company);
        return "Successfully";
    }

    @Override
    public String editCompany(CompanyDto companyDto,Long id) {
     return null;
    }
}
