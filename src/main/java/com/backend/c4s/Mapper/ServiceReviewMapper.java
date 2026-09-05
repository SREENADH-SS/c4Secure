package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.ServiceReview.AdminServiceReviewResponse;
import com.backend.c4s.Dto.ServiceReview.PublicServiceReviewResponse;
import com.backend.c4s.Entity.ServiceReview;
import com.backend.c4s.Entity.common.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceReviewMapper {

    public AdminServiceReviewResponse toAdminServiceReviewResponse(ServiceReview serviceReview){
        if (serviceReview==null){
            return null;
        }
        return AdminServiceReviewResponse.builder()
                .id(serviceReview.getId())
                .userId(serviceReview.getUser()!=null ? serviceReview.getUser().getId():null)
                .userName(fullName(serviceReview))
                .serviceType(determinServiceType(serviceReview))
                .serviceId(extractServiceId(serviceReview))
                .rating(serviceReview.getRating())
                .comment(serviceReview.getComment())
                .createdAt(serviceReview.getCreatedAt())
                .build();
    }

    public PublicServiceReviewResponse toPublicServiceReviewResponse(ServiceReview serviceReview){
        if (serviceReview==null){
            return null;
        }

        return PublicServiceReviewResponse.builder()
                .id(serviceReview.getId())
                .userName(fullName(serviceReview))
                .serviceType(determinServiceType(serviceReview))
                .serviceId(extractServiceId(serviceReview))
                .rating(serviceReview.getRating())
                .comment(serviceReview.getComment())
                .createdAt(serviceReview.getCreatedAt())
                .build();

    }

    private Long extractServiceId(ServiceReview serviceReview) {
        if (serviceReview==null){
            return null;
        }
        if (serviceReview.getMaintenance()!=null){
            return serviceReview.getMaintenance().getId();
        }
        if (serviceReview.getInstallation()!=null){
            return serviceReview.getInstallation().getId();
        }
        return null;
    }

    private ServiceType determinServiceType(ServiceReview serviceReview) {
        if (serviceReview==null){
            return null;
        }
        if (serviceReview.getMaintenance()!=null){
            return ServiceType.MAINTENANCE;
        }
        if (serviceReview.getInstallation()!=null){
            return ServiceType.INSTALLATION;
        }
        return null;
    }

    private String fullName(ServiceReview serviceReview) {
        if (serviceReview==null || serviceReview.getUser()==null){
            return "Anonymous";
        }
        String firstName=serviceReview.getUser().getFirstName()!=null ? serviceReview.getUser().getFirstName():"";
        String lastName=serviceReview.getUser().getLastName()!=null ? serviceReview.getUser().getLastName():"";

        String name=(firstName+" "+lastName).trim();
        return name.isEmpty() ? "Anonymous": name;
    }
}
