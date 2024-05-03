package com.backend.stayEasy.dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
	private UUID propertyId;
	private String propertyName;
    private String description;
    private String thumbnail;
    private String address;
    private int numBedRoom;
	private int numBathRoom;
    private Float price;
    private boolean isNull;
    private int numGuests;
    private int serviceFee;
    private int discount;
    private LocalDateTime createAt;
    private Float rating;
    private UserDTO owner;
    private List<ImagesDTO> imagesList;
    private List<CategoryDTO> categories;
    private Set<FeedbackDTO> feedbackList;
    private List<PropertyUtilitiesDTO> propertyUtilitis;
    private List<LikeRequestDTO> likeList;
    private List<RulesDTO> rulesList;
}