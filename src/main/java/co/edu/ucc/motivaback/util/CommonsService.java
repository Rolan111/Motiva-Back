package co.edu.ucc.motivaback.util;

import co.edu.ucc.motivaback.service.impl.FirebaseInitializer;
import com.google.cloud.firestore.CollectionReference;
import com.google.gson.Gson;

/**
 * @author nagredo
 * @project motiva-back
 * @class ConstantsFields
 */
public class CommonsService {

    private CommonsService() {
    }

    // Common Fields
    public static final String ID_QUESTION = "id_question";

    // Answer Fields Dto
    public static final String ANSWER = "answer";
    public static final String ID_ANSWER = "id_answer";
    public static final String ID_OPTION_ANSWERS = "id_option_answers";
    public static final String OPEN_ANSWER = "open_answer";
    public static final String ID_POLL = "id_poll";

    // Question Fields Dto
    public static final String QUESTION = "question";
    public static final String DESCRIPTION = "description";
    public static final String ID_FATHER = "id_father";
    public static final String ID_QUESTION_TYPE = "id_question_type";
    public static final String ORDER = "order";

    // User Fields Dto
    public static final String USER = "user";
    public static final String USER_NOT_FOUND = "El usuario no existe";
    public static final String LAST_NAME = "last_name";
    public static final String ID_SUPERVISOR = "id_supervisor";

    // Tracking Fields Dto
    public static final String COLLECTION_NAME_TRACKING_SHEET = "tracking_sheet";
    public static final String NAMES = "names";
    public static final String LASTNAMES = "lastnames";
    public static final String IDENTIFICATION_TYPE = "identificationType";
    public static final String IDENTIFICATION = "identification";
    public static final String ATTENTION_STATUS = "attention_status";
    public static final String TYPE_ROUTE = "type_route";
    public static final String REFERRED_ENTITY = "referred_entity";
    public static final String RECOMMENDATIONS = "recommendations";

    //RepComAgent Fields Dto
    public static final String COLLECTION_NAME_REP_COM_AGENT = "rep_com_agent";
    public static final String ACTIVITY_NAME = "name";
    public static final String ACTIVITY_NUMBER = "activity_number";

    public static final String DURATION = "duration";
    public static final String PLACE = "place";
    public static final String NUMBER_ATTENDEES = "number_attendees";
    public static final String ACTIVITY_OBJECTIVES = "activity_objectives";
    public static final String RESOURCES_USED = "resources_used";
    public static final String METHODOLOGY_USED = "methodology_used";
    public static final String ACTIVITY_DESCRIPTION_DEVELOPMENT = "activity_description_development";
    public static final String RESOURCES_OBTAINED = "resources_obtained";
    public static final String ACTIVITY_PROFESSIONAL_INCHARGE = "activity_professional_incharge";

    // Poll Fields Dto
    public static final String POLL = "poll";
    public static final String DATE = "date";
    public static final String EVIDENCE = "evidence";
    public static final String ID_USER = "id_user";

    // Option Answers Dto
    public static final String OPTION_ANSWER = "option_answer";
    public static final String ID_OPTION_ANSWER = "id_option_answer";

    // Msg
    public static final String NOT_ACCESS = "Acceso no autorizado";
    private static final Gson gson = new Gson();
    public static final String UPDATED_FAIL = "updated FAIL";
    public static final String CREATED_FAIL = "created FAIL";
    public static final String DELETED_FAIL = "deleted FAIL";
    public static final String CREATED_OK = "created OK";
    public static final String UPDATED_OK = "updated OK";
    public static final String DELETED_OK = "deleted OK";
    public static final String EMPTY_LIST = "empty list";
    public static final String LIST_OK = "list response OK";
    public static final String FOUND_OBJECT = "found object";
    public static final String NOT_FOUND_OBJECT = "not found object";

    public static Gson getGson() {
        return gson;
    }

    public static CollectionReference getFirebaseCollection(FirebaseInitializer firebase, String collectionName) {
        return firebase.getFirestore().collection(collectionName);
    }

}
