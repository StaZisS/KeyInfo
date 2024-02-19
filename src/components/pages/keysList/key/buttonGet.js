import {Button} from "react-bootstrap";

import('../../../../styles/keysItem.css')

export const ButtonGet = ({callback}) => {
    return(
        <Button onClick={() => callback } className="btn-sm me-3 rounded-5 get border-0 btn-success">
            <span className=' buttonName'>Отдать</span>
        </Button>
    )
}