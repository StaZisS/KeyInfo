import {
    Button,
    Col,
    Container,
    Modal, ModalBody,
    ModalHeader,
    Row
} from "react-bootstrap";
import DateHelper from "../../helpers/DateHelper";
import {useMutation, useQueryClient} from "react-query";
import KeyService from "../../services/KeyService";
import styles from "../pages/keysList/key/style.module.css"

export const AcceptedApplicationsModal = (props) => {

    const queryClient = useQueryClient()
    const mutation = useMutation((keyHolderId) => KeyService.giveKey(props.key_id,keyHolderId),{
        onSuccess(){
            queryClient.invalidateQueries(['keys'])
        }
    })

    const handleGet = (keyHolderId) => {
        mutation.mutate(keyHolderId)
        props.onHide()
    }

    return (
        <Modal {...props} size={'lg'} centered autoFocus={true}>
            <ModalHeader className='d-flex'>
                <div className='d-flex gap-3'>
                    <Modal.Title>Корпус {props.build}</Modal.Title>
                    <Modal.Title>Аудитория {props.room}</Modal.Title>
                </div>
                <Modal.Title>Одобренные заявки</Modal.Title>
            </ModalHeader>

            <ModalBody>
                <Container>
                    <Row className={'d-flex text-start border-bottom mb-1 w-100'}>
                        <div className={`fw-bold ${styles.wrap_title} `}>Дата</div>
                        <div className={`fw-bold ${styles.wrap_title}`}>Время</div>
                        <div className={`fw-bold ${styles.wrap_title}`}>Email</div>
                        <div className={`fw-bold ${styles.wrap_title}`}>ФИО</div>
                        {/* <Col className={`fw-bold `}></Col> */}
                    </Row>
                </Container>
                <Container>
                    {props.data.map((app) => {
                        const startTime = DateHelper.normalizeDate(app.start_time)
                        const endTime = DateHelper.normalizeDate(app.end_time)
                        return (
                            <Row key={app.application_id}
                                className='text-start border-bottom mb-1 d-flex align-items-center flex-wrap'>
                                <div className={`d-flex ${styles.wrapper}`}>
                                <Col className={`flex-grow-1`}>
                                    {startTime.day}.{startTime.month}.{startTime.year}
                                </Col>
                                <Col className={`flex-grow-1 ${styles.wrap}`}>
                                    {startTime.hours}:{startTime.minutes} - {endTime.hours}:{endTime.minutes}
                                </Col>

                                <Col className={`flex-grow-1 ${styles.wrap}`}>
                                    {app.owner_email}
                                </Col>
                                <Col className={`flex-grow-1 ${styles.wrap}`}>
                                {app.owner_name}
                                </Col>
                                </div>
                                
                                <Col className={`flex-grow-1  ${styles.wrap_button} d-flex `}>
                                    <Button onClick={() => handleGet(app.owner_id)} className={`btn px-4 rounded-5 btn-success mb-1 ${styles.modal_button}`}>
                                        Отдать
                                    </Button>
                                </Col>
                            </Row>

                        )
                    })}

                </Container>
            </ModalBody>
        </Modal>
    )
}