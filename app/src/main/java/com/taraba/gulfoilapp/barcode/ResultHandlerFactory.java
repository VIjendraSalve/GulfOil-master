package com.taraba.gulfoilapp.barcode;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

/**
 * Created by android3 on 12/31/15.
 */
public final class ResultHandlerFactory {
    private ResultHandlerFactory() {
    }

    public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
        ParsedResult result = parseResult(rawResult);
        switch (result.getType()) {
            case ADDRESSBOOK:
                //return new AddressBookResultHandler(activity, result);
            case EMAIL_ADDRESS:
                //return new EmailAddressResultHandler(activity, result);
            case PRODUCT:
                //return new ProductResultHandler(activity, result, rawResult);
            case URI:
                // return new URIResultHandler(activity, result);
            case WIFI:
                //return new WifiResultHandler(activity, result);
            case GEO:
                //return new GeoResultHandler(activity, result);
            case TEL:
                // return new TelResultHandler(activity, result);
            case SMS:
                //return new SMSResultHandler(activity, result);
            case CALENDAR:
                //return new CalendarResultHandler(activity, result);
            case ISBN:
                //return new ISBNResultHandler(activity, result, rawResult);
            default:
                return new TextResultHandler(activity, result, rawResult);
        }
    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }
}
