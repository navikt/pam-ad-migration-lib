package no.nav.pam.ad.migration.entity;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemarkTypeListConverter implements AttributeConverter<List<RemarkType>, String> {

    @Override
    public String convertToDatabaseColumn(List<RemarkType> remarkTypes) {
        if (remarkTypes == null || remarkTypes.isEmpty()) {
            return "";
        }
        return StringUtils.join(remarkTypes,",");
    }

    @Override
    public List<RemarkType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
                .map(RemarkType::findName)
                .collect(Collectors.toList());
    }

}
