package com.uni.jobofferprocessor.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferRequest {
    @Valid
    private AloBgRequest aloBg;
    @Valid
    private OlxRequest olx;
    @Valid
    private JobsBgRequest jobsBg;
    @Valid
    private ZaplataBgRequest zaplataBg;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class AloBgRequest {
        @NotNull
        private Integer size;
        @NotNull
        private Integer locationId;
        @NotNull
        private Integer categoryId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class OlxRequest {
        @NotNull
        private Integer size;
        @NotBlank
        private String locationName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class JobsBgRequest {
        @NotNull
        private Integer size;
        @NotNull
        private Integer locationId;
        @NotNull
        private Integer categoryId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ZaplataBgRequest {
        @NotNull
        private Integer size;
        @NotBlank
        private String locationName;
        @NotNull
        private Integer categoryId;
    }
}
