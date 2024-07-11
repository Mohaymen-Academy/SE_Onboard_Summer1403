package search_engine.query_decoder;

import org.apache.commons.collections4.CollectionUtils;
import search_engine.enums.Status;

import java.util.List;

public record Query(
        List<String> includes,
        List<String> optionals,
        List<String> excludes
) {
    public Status getStatus() {
        if (CollectionUtils.isEmpty(this.includes())) {
            if (CollectionUtils.isEmpty(this.optionals())) {
                if (!CollectionUtils.isEmpty(this.excludes()))
                    return Status.JUST_EXCLUDES;
                else
                    return Status.EMPTY;
            } else return Status.JUST_OPTIONAL;
        } else {
            if (!CollectionUtils.isEmpty(this.optionals())) {
                return Status.HAVE_OPTIONALS;
            }
            return Status.JUST_INCLUDES;
        }
    }
}
