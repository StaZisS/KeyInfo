import {Button} from "react-bootstrap";

import('../../../../styles/keysItem.css')

export const ButtonDel = () => {
    return(
        <Button className="btn-sm me-3 rounded-5 delit border-0 btn-success">
            <span className=' buttonName'>Забрать</span>
        </Button>
    )
}