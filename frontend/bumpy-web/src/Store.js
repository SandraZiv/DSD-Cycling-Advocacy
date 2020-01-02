import React, {useState} from "react";

export const UuidContext = React.createContext('');

const Store = ({children}) => {
    const [uuid, setUuid] = useState('');

    return (
        <UuidContext.Provider value={[uuid, setUuid]}>
           {children}
        </UuidContext.Provider>
    );
};

export default Store;
