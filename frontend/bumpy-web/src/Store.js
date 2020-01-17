import React, {useState} from "react";

export const ShortUuidContext = React.createContext('');

const Store = ({children}) => {
    const [shortUuid, setShortUuid] = useState('');

    return (
        <ShortUuidContext.Provider value={[shortUuid, setShortUuid]}>
            {children}
        </ShortUuidContext.Provider>
    );
};

export default Store;
