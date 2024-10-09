package com.jumkid.share.service.dto;

import com.jumkid.share.model.GenericEntity;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GenericDTOHandler {
    protected final UserProfileManager userProfileManager;

    protected GenericDTOHandler(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void normalizeOwnership(GenericDTO dto, GenericEntity oldEntity) {

        LocalDateTime now = LocalDateTime.now();
        UserProfile userProfile = userProfileManager.fetchUserProfile();
        String userId = userProfile.getId();

        if (oldEntity != null) {
            dto.setCreatedBy(oldEntity.getCreatedBy());
            dto.setCreatedOn(oldEntity.getCreatedOn());
        } else {
            dto.setCreatedBy(userId);
            dto.setCreatedOn(now);
        }

        dto.setModifiedBy(userId);
    }
}
