package kwetter.utils;

import java.util.HashSet;

/**
 * Created by Niek on 15/02/14.
 */
public class CaseInsensitiveSet extends HashSet<String>
{
    @Override
    public boolean add(String value){
        return super.add(value.toLowerCase());
    }
}
