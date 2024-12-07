package com.bujdi.carRecords.utils;

import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class ModelTypeMapper {
    private static final Map<String, Class<?>> MODEL_MAP = new HashMap<>();

    static {
        MODEL_MAP.put("maintenance_record", MaintenanceRecord.class);
        MODEL_MAP.put("vehicle", Vehicle.class);
    }

    public static Class<?> getModelClass(String modelType) {
        return MODEL_MAP.get(modelType);
    }
}
