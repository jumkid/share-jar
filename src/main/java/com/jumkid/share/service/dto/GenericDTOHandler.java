package com.jumkid.share.service.dto;

import com.jumkid.share.exception.ModificationDatetimeOutdatedException;
import com.jumkid.share.model.GenericEntity;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
            if (dto.getModifiedOn() == null || (oldEntity.getModifiedOn() != null
                    && !oldEntity.getModifiedOn().truncatedTo(ChronoUnit.MILLIS)
                    .equals(dto.getModifiedOn().truncatedTo(ChronoUnit.MILLIS)))) {
                throw new ModificationDatetimeOutdatedException();
            }
            dto.setCreatedBy(oldEntity.getCreatedBy());
            dto.setCreatedOn(oldEntity.getCreatedOn());
        } else {
            dto.setCreatedBy(userId);
            dto.setCreatedOn(now);
        }

        dto.setModifiedBy(userId);
        dto.setModifiedOn(now.truncatedTo(ChronoUnit.MILLIS));
    }
}
